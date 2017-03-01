# Hunspell4Vie
Example Java source code show how to use Hunspell, specifically for Vietnamese.
# Table of Contents
[Download](#download)
[Usesage](#useage)
[Dependency](#dependency)
[License](#license)

<a name="download"/>
# Download
- Clone with github and import to [Eclipse IDE](https://eclipse.org/).
- Download compiled [hunspell4Vie.jar](https://github.com/thieunguyenhung/hunspell4vie/raw/master/Hunspell4Vie/dist/hunspell4Vie.jar) file (required [jna.jar](http://repo1.maven.org/maven2/net/java/dev/jna/jna/4.3.0/jna-4.3.0.jar) at least 4.3.0)

<a name="useage"/>
# Usesage
This library included [hunspell-native-library](https://github.com/languagetool-org/languagetool/tree/master/hunspell-native-libs/libs/native-lib) and Vietnamese dictionary, you can also use your own native library or [other language dictionaries](https://github.com/titoBouzout/Dictionaries).

### Check misspelled word
Use checkSpell method from SpellChecker class, rmember to choose correct OS you are using.
```java
SpellChecker spellChecker= new SpellChecker(OSArchitecture.MACOS_64);
System.out.println(spellChecker.checkSpell("công việ", false));
```
This method will check word by word to detect error if this word does not exist in dictionary, then get suggestion from hunspell and replace misspelled word.

Second parameter specific which suggestion algorithm you want to use

 - `false` to use Hunspell4Vie.
 - `true` if you want to use default Hunspell suggestion.

We are working on making our suggestion algorithm more correct.

<a name="dependency"/>
# Dependency
- JNA (to run Hunspell)


<a name="license"/>
# License

  - This library using [Hunspell](https://github.com/hunspell/hunspell) and [JNA](https://github.com/java-native-access/jna) libraries.
  - Apache License version 2.0.
