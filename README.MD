# Recherche Operationnelle - M1 BDMA
## Présentation du Sujet

## Travail à faire

## Utilisation d'une machine personnelle
You should have Maven installed and an updated JDK.

Download Project
```shell
git clone https://github.com/AlexChanson/RechercheOperationnelle-M1-BDMA
```
Move to project and test build
```shell
cd .\RechercheOperationnelle-M1-BDMA\
mvn package
```

### Run your code 
```shell
java -cp target/TP-RO-M1-BDMA-1.0-SNAPSHOT.jar com.alexscode.teaching.Main
```

## Utilisation d'une machine en salle TP
### Ouvrir powershell
Set proxy for git
```
git config --global https.proxy http://proxy:3128
git config --global http.proxy http://proxy:3128
```

Download Project
```shell
git clone https://github.com/AlexChanson/RechercheOperationnelle-M1-BDMA
```

Setup maven
```
wget https://dlcdn.apache.org/maven/maven-3/3.8.6/binaries/apache-maven-3.8.6-bin.zip -UseBasicParsing -O maven.zip
Expand-Archive .\maven.zip .
cp .\RechercheOperationnelle-M1-BDMA\confing\settings.xml $Env:userprofile\.m2\settings.xml
$env:Path += 'U:\apache-maven-3.8.6\bin'
```
Only use last line if you have already downloaded

Check if it's working with `mvn --version`

Setup Java Environment
```
$Env:JAVA_HOME = "C:\Program Files\Java\jdk1.8.0_341\"
```

Move to project and test build
```shell
cd .\RechercheOperationnelle-M1-BDMA\
mvn package
```

### Run your code
```shell
java -cp target/TP-RO-M1-BDMA-1.0-SNAPSHOT.jar com.alexscode.teaching.Main
```



