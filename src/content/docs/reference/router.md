---
title: Router
---

A MESH Lib router consists of a [rule](/reference/rules) and a [handler](/reference/handlers). Together they define how a request is handled.

Routers are loaded from a `routers` folder next to the main config file. Some examples I use for testing the mod can be generated using the `/meshlib example` command.

A basic router may look something like this:
```json
{
    "rule": {
        "path": "/hello",
        "type": "path"
    },
    "handler": {
        "content": "Hello World!",
        "type": "static-content"
    }
}
```