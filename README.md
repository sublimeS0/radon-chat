# Radon Chat

A simple, lightweight chat mod designed to improve readability on large servers.

## Setup

Find the release corresponding to your game version and copy into `mods` folder. Like any other Fabric mod.

Create a `.json` file formatted like below. The groups require a hex `color` and list of `member`s to function. `name` 
is optional and only used for personal organization.

```
{
    "groups": [
        {
            "name": "friends",
            "color": "#008000",
            "members": [
                "sublimeS0",
                "Caup"
            ]
        },
        {
            "name": "enemies",
            "color": "#ff0000",
            "members": [
                "Notch",
                "Herobrine"
            ]
        }
    ]
}
```

This file can be located anywhere, but by default it is 
`C:\Users\<user>\AppData\Roaming\.minecraft\config\radon-chat_groups.json`. This can be changed in the mod's in-game 
config.

## License

This mod is available under the GPL 3.0 license.

## Issues

If any problems or bugs are found, feel free to open an issue. I'll check it out!
