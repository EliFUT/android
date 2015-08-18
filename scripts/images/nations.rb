require 'open-uri'

nations = [1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 63, 66, 68, 70, 72, 73, 77, 78, 79, 80, 81, 82, 83, 85, 87, 89, 90, 92, 93, 95, 97, 98, 99, 101, 102, 103, 104, 105, 107, 108, 110, 111, 112, 115, 116, 117, 118, 119, 120, 122, 123, 124, 126, 127, 129, 130, 132, 133, 136, 138, 140, 144, 145, 146, 147, 148, 150, 154, 155, 157, 161, 162, 163, 164, 165, 166, 167, 168, 171, 178, 180, 181, 183, 186, 188, 189, 191, 195, 197, 198, 208, 214, 215, 219]

nations.each do |id|
  `curl http://fifa15.content.easports.com/fifa/fltOnlineAssets/8D941B48-51BB-4B87-960A-06A61A62EBC0/2015/fut/items/images/cardflagslarge/web/#{id}.png > app/src/main/res/drawable-xhdpi/nation_#{id}.png`
  `curl http://fifa15.content.easports.com/fifa/fltOnlineAssets/8D941B48-51BB-4B87-960A-06A61A62EBC0/2015/fut/items/images/cardflagssmall/web/#{id}.png > app/src/main/res/drawable-hdpi/nation_#{id}.png`
end