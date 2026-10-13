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
    # Builds everything, then collects the distributable jars (per version).
    build.exec = ''
      set -eu
      ./gradlew build
      rm -rf dist && mkdir dist
      chest=$(sed -n 's/^ironchest_version=//p' gradle.properties)
      core=$(sed -n 's/^ironcore_version=//p' gradle.properties)
      # Supported versions, derived from the mc.* keys in gradle.properties.
      lines=$(cut -d. -f1-3 gradle.properties | grep '^mc[.]' | cut -d. -f2- | sort -uV)
      for line in $lines; do
        cp "iron-chests/fabric/versions/$line/build/libs/IronChests-$chest+$line.jar" "dist/IronChests-$chest+$line-Fabric.jar"
        cp "iron-chests/neoforge/versions/$line/build/libs/IronChests-neoforge-$chest+$line.jar" "dist/IronChests-$chest+$line-NeoForge.jar"
        cp "iron-core/fabric/versions/$line/build/libs/IronCore-$core+$line.jar" "dist/IronCore-$core+$line-Fabric.jar"
        cp "iron-core/neoforge/versions/$line/build/libs/IronCore-neoforge-$core+$line.jar" "dist/IronCore-$core+$line-NeoForge.jar"
      done
      ls dist
    '';
    check.exec = "./gradlew check";
    clean.exec = "./gradlew clean";
    test.exec = "./gradlew test";
    # Resolves [--version <version>] to a supported Minecraft version for run tasks.
    # Defaults to the Stonecutter active version.
    mc-version.exec = ''
      set -eu
      version=""
      while [ $# -gt 0 ]; do
        case "$1" in
          --version)
            [ $# -ge 2 ] || { echo "--version needs a value" >&2; exit 1; }
            version="$2"
            shift 2
            ;;
          *)
            echo "usage: $0 [--version <version>]" >&2
            exit 1
            ;;
        esac
      done
      lines=$(cut -d. -f1-3 gradle.properties | grep '^mc[.]' | cut -d. -f2- | sort -uV)
      if [ -z "$version" ]; then
        version=$(grep -F '[SC]' iron-chests/stonecutter.gradle | head -1 | sed "s/.*active '//;s/'.*//")
      fi
      if printf '%s\n' "$lines" | grep -qxF "$version"; then
        echo "$version"
      else
        echo "unknown version '$version' (available: $(printf '%s ' $lines))" >&2
        exit 1
      fi
    '';
    # Game runs default to the active version; pass --version to override.
    run-client.exec = ''
      active=$(mc-version "$@") && ./gradlew ":iron-chests:fabric:$active:runClient"
    '';
    run-server.exec = ''
      active=$(mc-version "$@") && ./gradlew ":iron-chests:fabric:$active:runServer"
    '';
    run-client-neoforge.exec = ''
      active=$(mc-version "$@") && ./gradlew ":iron-chests:neoforge:$active:runClient"
    '';
    run-server-neoforge.exec = ''
      active=$(mc-version "$@") && ./gradlew ":iron-chests:neoforge:$active:runServer"
    '';
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
    nixfmt.enable = true;
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
