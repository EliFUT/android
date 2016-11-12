package com.felipecsl.elifut.util;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.GamesActivityResultCodes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.IntentSender;
import android.util.Log;

import com.felipecsl.elifut.R;

public class BaseGameUtils {
  private static final String TAG = "BaseGameUtils";

  /**
   * Show a {@link android.app.Dialog} with the correct message for a connection error.
   *
   * @param activity         the Activity in which the Dialog should be displayed.
   * @param requestCode      the request code from onActivityResult.
   * @param actResp          the response code from onActivityResult.
   * @param errorDescription the resource id of a String for a generic error message.
   */
  public static void showActivityResultError(Activity activity, int requestCode, int actResp,
      int errorDescription) {
    if (activity == null) {
      Log.e(TAG, "*** No Activity. Can't show failure dialog!");
      return;
    }
    Dialog errorDialog;

    switch (actResp) {
      case GamesActivityResultCodes.RESULT_APP_MISCONFIGURED:
        errorDialog = makeSimpleDialog(activity,
            activity.getString(R.string.app_misconfigured));
        break;
      case GamesActivityResultCodes.RESULT_SIGN_IN_FAILED:
        errorDialog = makeSimpleDialog(activity,
            activity.getString(R.string.sign_in_failed));
        break;
      case GamesActivityResultCodes.RESULT_LICENSE_FAILED:
        errorDialog = makeSimpleDialog(activity,
            activity.getString(R.string.license_failed));
        break;
      default:
        // No meaningful Activity response code, so generate default Google
        // Play services dialog
        final int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
            activity, requestCode, null);
        if (errorDialog == null) {
          // get fallback dialog
          Log.e(TAG,
              "No standard error dialog available. Making fallback dialog.");
          errorDialog = makeSimpleDialog(activity, activity.getString(errorDescription));
        }
    }

    errorDialog.show();
  }

  /**
   * Create a simple {@link Dialog} with an 'OK' button and a message.
   *
   * @param activity the Activity in which the Dialog should be displayed.
   * @param text     the message to display on the Dialog.
   * @return an instance of {@link android.app.AlertDialog}
   */
  public static Dialog makeSimpleDialog(Activity activity, String text) {
    return new AlertDialog.Builder(activity)
        .setMessage(text)
        .setNeutralButton(android.R.string.ok, null)
        .create();
  }

  /**
   * Resolve a connection failure from {@link GoogleApiClient.OnConnectionFailedListener#onConnectionFailed(ConnectionResult)}
   *
   * @param activity             the Activity trying to resolve the connection failure.
   * @param client               the GoogleAPIClient instance of the Activity.
   * @param result               the ConnectionResult received by the Activity.
   * @param requestCode          a request code which the calling Activity can use to identify the
   *                             result of this resolution in onActivityResult.
   * @param fallbackErrorMessage a generic error message to display if the failure cannot be
   *                             resolved.
   * @return true if the connection failure is resolved, false otherwise.
   */
  public static boolean resolveConnectionFailure(Activity activity, GoogleApiClient client,
      ConnectionResult result, int requestCode, String fallbackErrorMessage) {
    if (result.hasResolution()) {
      try {
        result.startResolutionForResult(activity, requestCode);
        return true;
      } catch (IntentSender.SendIntentException e) {
        // The intent was canceled before it was sent.  Return to the default
        // state and attempt to connect to get an updated ConnectionResult.
        client.connect();
        return false;
      }
    } else {
      // not resolvable... so show an error message
      int errorCode = result.getErrorCode();
      Dialog dialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activity, requestCode);
      if (dialog != null) {
        dialog.show();
      } else {
        // no built-in dialog: show the fallback error message
        showAlert(activity, fallbackErrorMessage);
      }
      return false;
    }
  }

  /**
   * Show an {@link android.app.AlertDialog} with an 'OK' button and a message.
   *
   * @param activity the Activity in which the Dialog should be displayed.
   * @param message  the message to display in the Dialog.
   */
  public static void showAlert(Activity activity, String message) {
    new AlertDialog.Builder(activity)
        .setMessage(message)
        .setNeutralButton(android.R.string.ok, null)
        .create()
        .show();
  }
}
