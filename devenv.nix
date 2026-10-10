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
    # Builds everything, then collects the 4 distributable jars
    build.exec = ''
      ./gradlew build
      rm -rf dist && mkdir dist
      chest=$(sed -n 's/^ironchest_version=//p' gradle.properties)
      core=$(sed -n 's/^ironcore_version=//p' gradle.properties)
      cp iron-chests/fabric/build/libs/IronChests-"$chest".jar "dist/IronChests-$chest-Fabric.jar"
      cp iron-chests/neoforge/build/libs/IronChests-neoforge-"$chest".jar "dist/IronChests-$chest-NeoForge.jar"
      cp iron-core/fabric/build/libs/IronCore-"$core".jar "dist/IronCore-$core-Fabric.jar"
      cp iron-core/neoforge/build/libs/IronCore-neoforge-"$core".jar "dist/IronCore-$core-NeoForge.jar"
      ls dist
    '';
    check.exec = "./gradlew check";
    clean.exec = "./gradlew clean";
    test.exec = "./gradlew test";
    run-client.exec = "./gradlew :iron-chests:fabric:runClient";
    run-server.exec = "./gradlew :iron-chests:fabric:runServer";
    run-client-neoforge.exec = "./gradlew :iron-chests:neoforge:runClient";
    run-server-neoforge.exec = "./gradlew :iron-chests:neoforge:runServer";
  };

  enterShell = ''
    # Stable JDK path for VSCode extensions (GUI launches don't inherit
    # devenv env, and the Gradle extension won't expand env vars in paths).
    # Settings point at this symlink, so no store hash is ever hardcoded.
    mkdir -p "$HOME/.local/share"
    ln -sfn "$JAVA_HOME" "$HOME/.local/share/devenv-jdk"
    echo "IronChests devenv: $(java -version 2>&1 | head -1)"
    echo "Build with './gradlew build' (or 'build'), test with './gradlew test' (or 'test')."
    echo "Run the game with './gradlew :iron-chests:fabric:runClient' (or 'run-client')."
  '';

  # https://devenv.sh/tests/
  enterTest = ''
    ./gradlew build --no-daemon
  '';

  # https://devenv.sh/git-hooks/
  git-hooks.hooks = {
    convco.enable = true;
    nixfmt-rfc-style.enable = true;
    google-java-format.enable = true;
    end-of-file-fixer.enable = true;
    trim-trailing-whitespace.enable = true;
    mixed-line-endings.enable = true;
    check-merge-conflicts.enable = true;
    detect-private-keys.enable = true;
    check-added-large-files.enable = true;
  };

  # See full reference at https://devenv.sh/reference/options/
}
