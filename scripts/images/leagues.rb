require 'open-uri'

leagues = [1, 4, 10, 13, 14, 16, 17, 19, 20, 31, 32, 39, 41, 50, 53, 54, 56, 60, 61, 65, 66, 67, 68, 76, 78, 80, 83, 189, 308, 335, 336, 341, 350, 351, 353, 382, 383, 384, 2000, 2028]

leagues.each do |id|
  `curl https://fifa15.content.easports.com/fifa/fltOnlineAssets/8D941B48-51BB-4B87-960A-06A61A62EBC0/2015/fut/items/images/leagueLogos_sm/web/l#{id}.png > app/src/main/res/drawable-xhdpi/league_#{id}.png`
end