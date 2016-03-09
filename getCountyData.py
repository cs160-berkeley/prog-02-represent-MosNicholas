
import json
import os
import googlemaps

curr_dir = os.getcwd()
OUTPUT_FILE = os.path.join(curr_dir, 'App/Represent/mobile/src/main/assets', 'county-lat-longs.json')
INPUT_FILE = os.path.join(curr_dir, 'App/Represent/mobile/src/main/assets', 'election-county-2012.json')
API_KEY_FILES = os.path.join(curr_dir, 'api_key.json')
data = []

with open(API_KEY_FILES) as apiKeyFile:
  apiKeyJson = json.load(apiKeyFile)

gmaps = googlemaps.Client(apiKeyJson['key'])

with open(INPUT_FILE, 'r') as jFile:
  inData = json.load(jFile)

for d in inData:
  if d['fips'] != 0:
    countyName, stateShort = d['county-name'], d['state-postal']
    try:
      countyData = gmaps.geocode("%s,%s" % (countyName, stateShort))
      latLong = countyData[0]['geometry']['location']
      countyMetaData = {
        'county': countyName,
        'state': stateShort,
        'lat': latLong['lat'],
        'lng': latLong['lng']
      }
      data.append(countyMetaData)
    except Exception, e:
      print "%s, %s failed " % (countyName, stateShort)

with open(OUTPUT_FILE, 'w') as outfile:
    json.dump(data, outfile)

