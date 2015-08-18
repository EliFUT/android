require 'open-uri'

total_pages = 731
1.upto(total_pages) do |page|
	`curl https://www.easports.com/fifa/ultimate-team/api/fut/item?jsonParamObject=%7B%22page%22:#{page}%7D > scripts/players_#{page}.json`
end