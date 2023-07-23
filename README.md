# README

## What ?

Simple implementation of Excel formula

## References

- https://support.microsoft.com/en-gb/office/using-structured-references-with-excel-tables-f5ed2452-2337-4f71-bed3-c8ae6d2b276e
- https://stackoverflow.com/questions/30976962/nested-boolean-expression-parser-using-antlr
- https://en.wikipedia.org/wiki/Operation_(mathematics)
- https://github.com/bkiers/Mu/tree/master
- https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigDecimal.html#%3Cinit%3E(java.lang.String)
- https://support.microsoft.com/en-us/office/excel-functions-alphabetical-b3944572-255d-4efb-bb96-c6d90033e188
- https://support.microsoft.com/en-us/office/logical-functions-reference-e093c192-278b-43f6-8c3a-b6ce299931f5
- https://support.microsoft.com/en-us/office/if-function-69aed7c9-4e8a-4755-a9bc-aa8bbff73be2
- https://support.microsoft.com/en-au/office/error-type-function-10958677-7c8d-44f7-ae77-b9a9ee6eefaa
- https://tomassetti.me/antlr-mega-tutorial/
- https://yvesmouafo.developpez.com/tutoriels/java/grammaire-antlr/

## Autosuggest

Reference: https://github.com/oranoran/antlr4-autosuggest

## Frontend

http://localhost:8080

## Swagger

http://localhost:8080/q/swagger-ui/

https://openapi-generator.tech/docs/generators

https://redux-toolkit.js.org/introduction/getting-started

https://redux-toolkit.js.org/introduction/getting-started#rtk-query

https://redux-toolkit.js.org/rtk-query/usage-with-typescript

https://react-redux.js.org/introduction/getting-started

https://github.com/amplitude/redux-query

https://amplitude.github.io/redux-query/docs/getting-started

https://github.com/krlls/react-redux-rxjs-ts

## Java 17

native build java version is 17

### Install

run `java -v` and `javac -v` if version is not 17 do the following commands:

```bash
sudo dnf install java-17-openjdk.x86_64 java-17-openjdk-devel.x86_64 -y
```

```bash
sudo alternatives --config java
```
> select java 17

```bash
sudo alternatives --config javac
```
> select java 17

### Define JAVA_HOME
only if `mvn -v` does not return it

```bash
echo "export JAVA_HOME=\"/usr/lib/jvm/java\"" >> ~/.bashrc
```