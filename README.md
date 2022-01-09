# Github Service
## Uruchomienie
Wymagania:
```shell script
- docker
- docker-compose
```
Obraz aplikacji jest dostępny w serwisie Dockerhub, więc aby uruchomić aplikację wystarczy użyć poniższej komendy:
```shell script
sudo docker-compose up
```

## Kompilacja (opcjonalnie)
Wymagania:
```shell script
- docker
- docker-compose
- maven
```

Jeżeli chcemy zwiększyć ilość możliwych do wykonania zapytań, należy uzyskać token do API Github'a i umieścić go w pliku `github.env`:
```shell script
#!/bin/bash
GITHUB_TOKEN="<token>"
```

Kompilacja i uruchomienie:
```shell script
./build.sh
sudo docker-compose up
```

## Rozwiązanie
Aplikacja obsługuje trzy różne zapytania:
* GET /users/{username}/repos
    * Zwraca listę repozytoriów dla danego użytkownika wraz z liczbą gwiazdek
    * per_page - liczba repozytoriów na stronę
    * page - numer strony
    * fields - dodatkowe pola, które mają zostać dołączone do wyników
    * exclude_forks - czy forkowane repozytoria mają być ignorowane
* GET /users/{username}/stars
    * Zwraca sumę gwiazdek we wszystkich repozytoriach
    * Flaga exclude_forks nie jest obsługiwana, ponieważ liczba gwiazdek w frokowanym repozytorium jest niezależna od oryginalnego
* GET /users/{username}/languages
    * Zwraca nazwy języków programowania wraz z liczbą bajtów kodu we wszystkich repozytoriach posortowaną malejąco
    * exclude_forks - czy forkowane repozytoria mają być ignorowane
    * count - liczba wyników
    
Rozwiązanie korzysta tylko z dwóch różnych zapytań udostępnionych w ramach API Github'a:
* /users/{username}/repos
* /repos/{username}/{repo}/languages

Przy założeniu, że użytkownik tego serwisu nie potrzebuje dostępu do najnowszych danych, została dodana warstwa do cachowania odpowiedzi z API, które mają ustawiony czas życia na 5 minut.
W ten sposób, w przypadku dużej ilości zapytań dotyczących tego samego użytkownika zmniejszamy znacząco ilość zapytań kosztem nieaktualnych danych (do 5 minut), co w przypadku używania API Githuba bez tokena ma duże znaczenie z powodu ograniczenia 60 zapytań na godzinę.
Jeżeli zajdzie potrzeba można zmniejszyć/zwiększyć czas życia wpisów.

### Co można zmienić?
Największą wadą w przypadku listowania języków dla danego użytkownika jest ilość zapytań, która jest potrzebna do uzyskania wyniku, jednak nie znaleziono żadnego rozwiązania oprócz cachowania.

### Możliwe rozszerzenia:
* Przepisania aplikacji przy użyciu programowania reaktywnego, aby zmniejszyć zużycie zasobów i zwiększyć liczbę możliwych do obsłużenia na raz zapytań.
* Dodanie obsługi organizacji/zespołów
