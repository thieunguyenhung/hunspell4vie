# Hunspell4Vie
Example Java source code show how to use Hunspell, specifically for Vietnamese.

# Download

  - Clone with github
  - Download jar file (required [jna.jar](http://repo1.maven.org/maven2/net/java/dev/jna/jna/4.3.0/jna-4.3.0.jar) atleast 4.3.0)

# Usesage
This library included [hunspell-native-library](https://github.com/languagetool-org/languagetool/tree/master/hunspell-native-libs/libs/native-lib) and Vietnamese dictionary, you can also use your own native library or [other language dictionaries](https://github.com/titoBouzout/Dictionaries).

## Check misspelled word
Use checkSpell method from SpellChecker class, rmember to choose correct OS you are using.
```java
SpellChecker spellChecker= new SpellChecker(OSArchitecture.MACOS_64);
System.out.println(spellChecker.checkSpell("công tac"));
```
This method will check word by word to detect error if this word does not exist in dictionary, then get suggestion from hunspell and replace misspelled word. We are working on making suggestion more correct.

License
----
This library using [Hunspell](https://github.com/hunspell/hunspell) and [JNA](https://github.com/java-native-access/jna) library.
Apache License version 2.0
