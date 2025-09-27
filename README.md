# GrainWeightApp & LoadTesterApp

## Přehled projektu
Tento projekt obsahuje dvě vzájemně propojené aplikace vytvořené v rámci bakalářské práce.  
Jejich cílem je umožnit praktické testování různých přístupů ke zpracování požadavků v prostředí **Spring Boot** a porovnání jejich výkonu a dopadu na využití hardwaru.

- **GrainWeightApp** – simulační aplikace pro evidenci vážení obilí a uživatelských záznamů. Slouží jako cílová aplikace, na kterou jsou směrovány zátěžové testy.  
- **LoadTesterApp** – nástroj pro generování testů, které posílají požadavky na GrainWeightApp, měří odezvy a sbírají hardwarové metriky.

---

## GrainWeightApp
GrainWeightApp je jednoduchý systém pro správu vážení obilí, postavený na Spring Bootu a Thymeleafu. Aplikace byla vytvořena jako školní projekt, jehož cílem bylo seznámení se se Spring Bootem. Aplikace je upravena pro účely BP.  
Umožňuje:
- správu uživatelů a záznamů vážení,
- zobrazení dat v jednoduchém webovém rozhraní.

Speciální část GrainWeightApp tvoří **endpointy `/api/work/...`**, které simulují různé způsoby zpracování požadavků.  
Každý požadavek může obsahovat parametry:
- `mode` – způsob zpracování,
- `size` – velikost poolu / limit souběhu,
- `delayMs` – simulace I/O čekání,

Při zpracování se měří:
- **queueWaitMs** – čas, jak dlouho požadavek čekal, než byl spuštěn,  
- **serverProcessingMs** – čas samotného zpracování uvnitř serveru.  

Tyto hodnoty jsou vraceny v odpovědi a slouží pro vyhodnocení testu.

---

## Způsoby zpracování požadavků
Aplikace podporuje **tři strategie**, které jsou implementovány jako samostatné třídy:

### 1. SerialStrategy
- Všechny požadavky se zpracovávají **sériově** (jeden po druhém).  
- Implementováno přes `Semaphore(1, fair=true)`.  

### 2. PoolStrategy
- Požadavky jsou rozdělovány do **pevného thread-poolu** o velikosti `size`.  
- Implementováno přes `Executors.newFixedThreadPool(size)`.  

### 3. VirtualStrategy
- Každý požadavek běží ve vlastním **virtuálním vlákně** (Java 21, Project Loom).  
- Současný počet běžících úloh je řízen semaforem (`Semaphore(cap)`), aby nedošlo k přetížení.  

---

## LoadTesterApp
LoadTesterApp je nástroj pro **zátěžové testování** GrainWeightApp. Používá **reaktivní programování (Reactor)** pro generování požadavků.

Umí:
- vytvářet testy (počet requestů, concurrency, režim zpracování, parametry zátěže),
- spouštět testy a odesílat tisíce požadavků,
- měřit latence na straně klienta,
- číst metriky z GrainWeightApp (serverProcessingMs, queueWaitMs),
- sbírat hardwarové metriky z actuator endpointů (CPU, paměť, heap),
- ukládat výsledky do databáze a generovat souhrnné statistiky.

Každý běh testu je identifikován unikátním **TestRun ID**, takže LoadTesterApp přesně ví, které výsledky patří ke kterému testu. Po dokončení běhu se uloží:
- souhrnné statistiky (průměrné a p95 latence, úspěšnost, throughput),
- hardware summary (průměrné/p95/max CPU a paměť),
- stav testu (FINISHED / FAILED).

---

## Výstupy testování
Na základě spuštěných testů lze:
- porovnávat jednotlivé strategie zpracování (serial vs. pool vs. virtual),
- sledovat, jak se liší odezvy při různé zátěži a konfiguraci,
- vyhodnotit dopad na využití CPU a paměti,
- identifikovat breakpointy (vysoký počet failed requestů), 
- analyzovat úzká hrdla (queue wait, délka serverového zpracování).
