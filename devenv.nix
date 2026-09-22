{ pkgs, lib, ... }:

# https://devenv.sh/basics/

let
  # Native runtime libs for `./gradlew runClient` (LWJGL/GLFW/OpenAL).
  # Mirrors what PrismLauncher puts on LD_LIBRARY_PATH in nixpkgs
  # (see pkgs/by-name/pr/prismlauncher/package.nix).
  # https://devenv.sh/packages/
  mcRuntimeLibs = with pkgs; [
    (lib.getLib stdenv.cc.cc)
    glfw3-minecraft
    openal

    # OpenAL backends
    alsa-lib
    libjack2
    libpulseaudio
    pipewire

    # GLFW / GL
    libGL
    xorg.libX11
    xorg.libXcursor
    xorg.libXext
    xorg.libXrandr
    xorg.libXxf86vm
    wayland
    libdecor

    udev # oshi
    vulkan-loader # VulkanMod's lwjgl
    flite # text-to-speech narrator

    # Optional: uncomment as needed
    # libusb1 # controller support
  ];
in
{
  # https://devenv.sh/languages/
  # The JDK major version must match the mod's toolchain, see build.gradle
  # and .github/workflows/build.yml.
  # https://devenv.sh/languages/java/
  languages.java = {
    enable = true;
    jdk.package = pkgs.jdk25;
    # Keep off: the Gradle wrapper pins the build's Gradle version, see
    # gradle/wrapper/gradle-wrapper.properties.
    gradle.enable = false;
  };

  # https://devenv.sh/packages/
  packages = [
    pkgs.git

    # Helpers LWJGL shells out to / probes for
    pkgs.pciutils # lspci
    pkgs.xorg.xrandr
  ]
  ++ mcRuntimeLibs;

  env.LD_LIBRARY_PATH = "/run/opengl-driver/lib:${lib.makeLibraryPath mcRuntimeLibs}";

  # https://devenv.sh/scripts/
  scripts = {
    build.exec = "./gradlew build";
    check.exec = "./gradlew check";
    clean.exec = "./gradlew clean";
    run-client.exec = "./gradlew runClient";
    run-server.exec = "./gradlew runServer";
  };

  enterShell = ''
    echo "IronChests devenv: $(java -version 2>&1 | head -1)"
    echo "Build with './gradlew build' (or 'build'), test with './gradlew check' (or 'check')."
    echo "Run the game with './gradlew runClient' (or 'run-client')."
  '';

  # https://devenv.sh/tests/
  enterTest = ''
    ./gradlew build --no-daemon
  '';

  # https://devenv.sh/git-hooks/
  # git-hooks.hooks.shellcheck.enable = true;

  # See full reference at https://devenv.sh/reference/options/
}
