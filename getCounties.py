
import json
import os

curr_dir = os.getcwd()
OUTPUT_FILE = os.path.join(curr_dir, 'App/Represent/mobile/src/main/assets', 'counties.json')
INPUT_FILE = os.path.join(curr_dir, 'App/Represent/mobile/src/main/assets', 'election-county-2012.json')
data = []

with open(INPUT_FILE, 'r') as jFile:
  inData = json.load(jFile)

for d in inData:
  if d['fips'] != 0:
    data.append(d['county-name'] + "," + d['state-postal'])

with open(OUTPUT_FILE, 'w') as outfile:
    json.dump(data, outfile)

