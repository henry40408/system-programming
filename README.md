## System Programming Project

## Requirements

1. JDK 1.7
2. Gradle, if you need to compile the jar file on your own.

## How to compile

```shell
$ gradle jar
```

Then you would get a jar file in `build/libs` called `compiler.jar`.

## How to run

```shell
$ java -jar build/libs/compiler.jar myprog.c myprog.asm
```

I assume you have `myprog.c` in the current directory, and be cautioned, **`myprog.asm` would be overwritten during the procedure**!

## License

© Heng-Yi Wu 2016.