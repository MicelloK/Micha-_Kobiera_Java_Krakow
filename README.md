# Podział koszyka

Biblioteka dzieląca przedmioty w koszyku klienta na grupy dostaw.

## API
Biblioteka zawiera jedną publiczną klasę `BasketSplitter`.

```java
public BasketSplitter(String absolutePathToConfigFile)
```

Do podzielenia koszyka klienta należy użyć publicznej metody `split`.
```java
public Map<String, List<String>> split(List<String> items)
```

Metoda ta przyjmuje listę przedmiotów w koszyku (`List<String>`)
i zwraca mapę (`Map<String, List<String>>`), w której kluczem jest gurpa dostaw, a wartością lista przedmiotów.

Nie zostało zaimplementowane sprawdzenie poprawności pliku konfiguracyjnego. Treść zadania zakłada poprawność tego pliku.

## Algorytm
Problem podzielenia koszyka przedmiotów na grupę dostaw to znany problem SetCover.
Szukanie optymalnego pokrycia jest NP-trudne ([Wikipedia](https://en.wikipedia.org/wiki/Set_cover_problem)).
Dlatego do rozwiązania został użyty zachłanny algorytm aproksymacyjny o złożoności *O(m\*n)*, gdzie *m* to liczba unikatowych kategorii, a *n* to liczba produktów.

### Podejście zachłanne
1. Znajdź kategorię, która zawiera najwięcej niepokrytych przemiotów z koszyka klienta;
2. Dodaj kategorię do wyniku;
3. Powtarzaj powyższe kroki, dopóki nie zostaną pokryte wszystkie przedmioty.

## Wymagania
Biblioteka została napisana w `OpenJDK 21.0.1` i zbudowana przy użyciu `gradle 8.2`.

### Zależności
Do testowania został wykorzystany `junit`, a do obsługi plików json - `json-simple`.

## Testy
Do oprogramowania zostały napisane testy jednostkowe.