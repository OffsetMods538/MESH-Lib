---
title: Getting Started
---

To use MESH Lib, you first need to download it and its dependencies. MESH Lib is available on Modrinth [here](https://modrinth.com/mod/mesh-lib).

After you have MESH Lib in your server's `mods` or `plugins` folder, you will need to configure it.

## Configuration
The config can be modified either manually or by using the `/meshlib config` command, which you can read more about [here](https://monkeylib538.docs.offsetmonkey538.top/whatever/path/goes/to/config/command/page).  // TODO  
The config file is located at `config/meshlib/main.json` on Fabric/NeoForge and `plugins/meshlib/main.json` on PaperMC.

The config contains two number values: `httpPort` and `exposedPort`:
- `httpPort` - the port MESH Lib will bind to.
- `exposedPort` - the port MESH Lib will be accessed from externally. Useful when the server is running behind some kind of proxy like docker, nginx, traefik, cloudflare tunnel, etc.
:::note
To use the Minecraft server's port, `httpPort` has to be the same as the port defined in `server.properties`. Many hosts will keep that as the default `25565`, but then expose it externally at a different port, in which case `exposedPort` should be set.
:::

After you've configured the mod, you will need to add a router that tells MESH Lib how requests should be handled.  
If you're using MESH Lib as a dependency for another mod like Git Pack Manager, you may not need to add a router manually if the dependant mod does it automatically.
