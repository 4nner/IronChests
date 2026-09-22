{
  pkgs,
  ...
}:

{
  # https://devenv.sh/basics/

  # https://devenv.sh/languages/
  # Java is required by the mod (see build.gradle: options.release,
  # sourceCompatibility/targetCompatibility, and CI).
  # The major Java Version should be the same across all of these.
  languages.java = {
    enable = true;
    jdk.package = pkgs.jdk25;
    # Keep false: the Gradle wrapper (gradle-9.5.1) pins the build's Gradle.
    # nixpkgs Gradle would be a different version.
    gradle.enable = false;
  };

  packages = [
    pkgs.git
  ];

  # https://devenv.sh/scripts/
  scripts = {
    build.exec = "./gradlew build";
    check.exec = "./gradlew check";
    clean.exec = "./gradlew clean";
  };

  # https://devenv.sh/basics/
  enterShell = ''
    echo "IronChests devenv: $(java -version 2>&1 | head -1)"
    echo "Build with './gradlew build' (or 'build'), test with './gradlew check' (or 'check')."
  '';

  # https://devenv.sh/tests/
  enterTest = ''
    ./gradlew build --no-daemon
  '';

  # https://devenv.sh/git-hooks/
  # git-hooks.hooks.shellcheck.enable = true;

  # See full reference at https://devenv.sh/reference/options/
}
