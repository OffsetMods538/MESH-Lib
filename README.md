# MESH Lib
[![discord-singular](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/social/discord-singular_vector.svg)](https://discord.offsetmonkey538.top/)
[![modrinth](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/mesh-lib)  

Easy to use library for running your HTTP server on the same port as the Minecraft server.  
Available for both fabric and paper.

Javadoc is available [here](https://maven.offsetmonkey538.top/javadoc/releases/top/offsetmonkey538/meshlib/mesh-lib/latest)

## Version Support
I am hoping to keep this mod supported for 1.19 up to latest.

## Usage
### Players
Players shouldn't ever need to install this mod.  
It should be JIJ-ed by mod devs

### Mod Devs

#### Gradle
Add my maven repo to your repositories block:
```groovy
repositories {
    // Others
    
    maven {
        name = "OffsetMods538"
        url = "https://maven.offsetmonkey538.top/releases"
        content {
            includeGroup "top.offsetmonkey538.meshlib"
        }
    }
}
```

This library is meant to be used as a JIJ (Jar-In-Jar), meaning you include it inside your mod/plugin.  
To do that you can use `include` for fabric and the shadow gradle plugin for paper:  
```groovy
dependencies {
    // For fabric
    include modImplementation("top.offsetmonkey538.meshlib:mesh-lib-fabric:1.0.3+1.21.4")
    
    // For paper
    implementation "top.offsetmonkey538.meshlib:mesh-lib-paper:1.0.3+1.21.4"
}
```
Make sure to use the latest version.

#### Using

Let's write a simple http server that will live at `http://server.com:25565/simple-server` and just serve `"Hello, World!"` in plain text!

First we'll need the actual `HttpHandler`, for that we'll create a new class, let's call it `MyHttpHandler`.  
This class has to implement the `HttpHandler` interface, this will look something like this:
```java
public class MyHttpHandler implements HttpHandler {

    @Override
    public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) throws Exception {
        // Logic will go here
    }
}
```

Now we'll need to actually implement the handler. You can google "HTTP Netty" for more info on how to handle HTTP requests with Netty.
```java
public void handleRequest(@NotNull ChannelHandlerContext ctx, @NotNull FullHttpRequest request) throws Exception {
    // Write "Hello, World!" to a buffer, encoded in UTF-8
    final ByteBuf content = Unpooled.copiedBuffer("Hello, World!", StandardCharsets.UTF_8);
    // Create a response with said buffer
    final FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, content);

    // Set the "CONTENT_TYPE" header to tell the browser that this is plain text encoded in UTF-8
    response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");


    // Send the response and close the connection
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
}
```

Finally, you'll need to actually register your handler. Put this in your mod or plugin initializer:
```java
@Override
public void onInitializeServer() {
    // Others
    
    HttpHandlerRegistry.INSTANCE.register("simple-server", new MyHttpHandler());
}
```
The id is the path that your handler will be able to listen on, in this case `server:port/simple-server`. For compatibility reasons, no mod is allowed to occupy the root path.  
If you need to listen to multiple paths (`server:port/simple-server/test` and `server:port/simple-server/test2`), then use `request.uri()` inside your handler, do **not** use `simple-server/test` as the id, it **will not** work.

Now, if you launch the server and try visiting `localhost:25565/simple-server` in your browser of choice, you should be greeted with a nice welcome message :D  
If not, then come yell at me on my [discord](http://discord.offsetmonkey538.top).
