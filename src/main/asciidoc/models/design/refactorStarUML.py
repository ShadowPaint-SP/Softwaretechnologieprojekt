import json


with open('src/main/asciidoc/models/design/Campingplatz_design.mdj', 'r') as f:
    data = json.loads(f.read())



data = json.dumps(data, indent=2)

with open('src/main/asciidoc/models/design/Campingplatz_design.mdj', 'w') as f:
    f.write(data)