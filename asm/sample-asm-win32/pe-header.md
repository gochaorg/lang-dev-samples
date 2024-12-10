**PE-заголовок** (Portable Executable Header) — это ключевая часть EXE или DLL файла, которая содержит информацию, необходимую операционной системе для загрузки и выполнения программы. PE-заголовок начинается со смещения, указанного в поле `e_lfanew` DOS-заголовка, и делится на несколько структур: **PE Signature**, **File Header**, **Optional Header**, а также сопровождается таблицей секций.

---

### Основная структура PE-заголовка

#### 1. PE Signature
- Первые 4 байта PE-заголовка содержат сигнатуру `PE\0\0` (в ASCII: `50 45 00 00`).
- Это идентификатор, который подтверждает, что файл использует формат Portable Executable.

#### 2. File Header (IMAGE_FILE_HEADER)
Эта структура содержит общую информацию о файле.

| Поле                     | Размер  | Описание                                                                                      |
|--------------------------|---------|----------------------------------------------------------------------------------------------|
| `Machine`                | 2 байта | Архитектура процессора: `0x014c` для x86, `0x8664` для x64.  <br> _14c_                                |
| `NumberOfSections`       | 2 байта | Количество секций в файле (например, `.text`, `.data`).  <br> _3_                                     |
| `TimeDateStamp`          | 4 байта | Время создания файла в формате UNIX Time. <br> _TimeDateStamp	08/21/2024 19:32:19_	|
| `PointerToSymbolTable`   | 4 байта | Смещение до таблицы символов (обычно 0, не используется).                                     |
| `NumberOfSymbols`        | 4 байта | Количество символов в таблице (обычно 0, не используется).                                    |
| `SizeOfOptionalHeader`   | 2 байта | Размер Optional Header, который следует за File Header. <br>  _E0h_                                     |
| `Characteristics`        | 2 байта | Флаги, описывающие характеристики файла (например, исполняемый файл, библиотека и т. д.).    |

---

#### 3. Optional Header (IMAGE_OPTIONAL_HEADER)
Эта структура является опциональной только формально — для исполняемых файлов Windows она обязательна. Здесь хранится важная информация о загрузке программы.

| Поле                          | Размер  | Описание                                                                                     |
|-------------------------------|---------|---------------------------------------------------------------------------------------------|
| `Magic`                       | 2 байта | Тип исполняемого файла: `0x10b` для 32-битных, `0x20b` для 64-битных файлов. <br> _10Bh_ |
| `MajorLinkerVersion`          | 1 байт  | Версия компоновщика.  <br> _BAh_ |
| `MinorLinkerVersion`          | 1 байт  | Версия компоновщика.  <br> _BBh_                                                                     |
| `SizeOfCode`                  | 4 байта | Размер секции `.text` в байтах.  <br> _200h_                                                           |
| `SizeOfInitializedData`       | 4 байта | Размер секций с инициализированными данными (например, `.data`). <br> _400h_                           |
| `SizeOfUninitializedData`     | 4 байта | Размер секций с неинициализированными данными (например, `.bss`).  <br> _0h_                         |
| `AddressOfEntryPoint`         | 4 байта | Адрес точки входа программы (относительно начала виртуальной памяти). <br> _1000h_                      |
| `BaseOfCode`                  | 4 байта | Базовый адрес секции `.text`.  <br> _1000h_                                                             |
| `ImageBase`                   | 4/8 байт| Предпочтительный базовый адрес загрузки в память (по умолчанию `0x00400000` для EXE). <br> _400000h_      |
| `SectionAlignment`            | 4 байта | Выравнивание секций в памяти. <br> _1000h_                                                            |
| `FileAlignment`               | 4 байта | Выравнивание данных в файле.  <br> _200h_                                                            |
| `MajorOperatingSystemVersion` | 2 байта | Минимальная версия ОС, необходимая для запуска. !!!  <br> _4_                                            |
| `SizeOfImage`                 | 4 байта | Полный размер образа в памяти (включая заголовки и секции).  <br> _4000h_                              |
| `SizeOfHeaders`               | 4 байта | Размер заголовков файла (выравненный).  <br>  _400h_                                                  |
| `NumberOfRvaAndSizes`         | 4 байта | Количество элементов в таблице Data Directory.                                             |

В заголовке PE (Portable Executable) файла все эти поля находятся в различных структурах, входящих в состав PE заголовка. Давайте разберем их подробнее, включая их описание и роль.

### PE-заголовок
Наиболее интересные поля находятся в **Optional Header** PE-заголовка. 

#### Описание полей
1. `MajorOperatingSystemVersion` / `MinorOperatingSystemVersion`
   - Версия операционной системы, для которой предназначен исполняемый файл.
   - Поля помогают определить совместимость файла с ОС.
   - Формат: два отдельных значения (Major и Minor).
   - MajorOperatingSystemVersion - 4 
   - MinorOperatingSystemVersion - 0 


2. `MajorImageVersion` / `MinorImageVersion`
   - Версия самого исполняемого файла.
   - Обычно используется разработчиками для управления версиями приложений.
   - MajorImageVersion - 0 
   - MinorImageVersion - 0 

3. `MajorSubsystemVersion` / `MinorSubsystemVersion`
   - Минимальная версия подсистемы (например, Windows), требуемая для запуска файла.
   - Например, `5.1` соответствует Windows XP, а `6.0` — Windows Vista.
   - MajorSubsystemVersion - 4
   = MinorSubsystemVersion - 0

4. `Win32VersionValue`
   - Зарезервировано для использования системой Windows, всегда 0.
   - Практически не используется.

5. `Subsystem`
   - Определяет тип подсистемы, для которой предназначен файл:
     - `1` — Driver (драйвер).
     - `2` — Windows GUI (графический интерфейс).
     - `3` — Windows CUI (консольное приложение).
     - И т. д.
     - Subsystem - WINDOWS_GUI (2)

6. `DllCharacteristics`
   - Флаги, указывающие различные характеристики DLL (если файл является DLL):
     - `IMAGE_DLLCHARACTERISTICS_DYNAMIC_BASE` — поддержка динамической адресации.
     - `IMAGE_DLLCHARACTERISTICS_NX_COMPAT` — поддержка защиты NX.
     - И другие.

7. `SizeOfStackReserve`
   - Количество байтов, зарезервированное для стека приложения.
   - SizeOfStackReserve - 100000h

8. `SizeOfStackCommit`
   - Количество байтов, выделяемое для стека сразу при запуске.
   - SizeOfStackCommit - 10000h

9. `SizeOfHeapReserve`
   - Количество байтов, зарезервированное для кучи.
   - SizeOfHeapReserve - 100000h

10. `SizeOfHeapCommit`
    - Количество байтов, выделяемое для кучи сразу при запуске.
    - SizeOfHeapCommit - 1000h

11. `LoaderFlags`
    - Зарезервированное поле. Всегда устанавливается в `0`.

12. `NumberOfRvaAndSizes`
    - Количество записей в таблице Data Directory.
    - Обычно фиксировано (16 для большинства PE файлов).

Начальное значение регистров стека и адреса начала стека и кучи в исполняемом файле **PE (Portable Executable)** определяются несколькими полями в заголовке PE, особенно в **Optional Header**. Вот как это работает:

---

### 1. Адрес стека
#### Поля:
- `SizeOfStackReserve`  
  - Указывает размер памяти, который будет зарезервирован для стека процесса (обычно большое значение, например, 1 МБ или 2 МБ).
- `SizeOfStackCommit`  
  - Указывает размер памяти, который будет фактически выделен для стека сразу при запуске (обычно меньше, например, 4 КБ или 8 КБ).

#### Как используется:
- ОС резервирует виртуальную память размером, указанным в `SizeOfStackReserve`, но изначально выделяет только `SizeOfStackCommit` (остальная память может быть выделена позднее при необходимости).
- Регистры стека (например, `ESP` на x86 или `RSP` на x64) инициализируются таким образом, чтобы указывать на верхний край выделенного сегмента памяти для стека.

---

### 2. Адрес кучи
#### Поля:
- `SizeOfHeapReserve`  
  - Указывает размер памяти, который будет зарезервирован для кучи (например, 1 МБ).
- `SizeOfHeapCommit`  
  - Указывает начальный размер выделенной памяти для кучи (например, 4 КБ).

#### Как используется:
- При создании процесса ОС создаёт основную кучу с размерами, указанными в этих полях.
- Адрес начала кучи не задаётся напрямую в PE-заголовке, он определяется динамически ОС при запуске приложения.

---

### 3. Настройка регистров (ESP / RSP)
На этапе загрузки:
- Адрес стека рассчитывается на основе значения поля `SizeOfStackReserve` и выделяется виртуальная память.
- Регистр стека (`ESP` или `RSP`) указывает на верхний адрес выделенного блока стека.

---

### 4. Управление стеком и кучей:
- Конкретные адреса стека и кучи вычисляются динамически загрузчиком ОС в зависимости от выделенного виртуального адресного пространства.
- Эти адреса не фиксированы в PE-файле, но указанные размеры резервирования (`SizeOfStackReserve` / `SizeOfHeapReserve`) и выделения (`SizeOfStackCommit` / `SizeOfHeapCommit`) задают ограничения на используемую память.


Начальное значение регистров `IP` (Instruction Pointer) и `CS` (Code Segment) в процессе выполнения **PE-файла** (Portable Executable) определяется загрузчиком ОС. Эти регистры указывают на адрес начала выполнения программы и связаны с определёнными полями в PE-заголовке.

---

### 1. Поля, влияющие на начальное значение `IP` и `CS`

#### `AddressOfEntryPoint`
- Смещение от начала секции `.text` (обычно основной секции кода) до инструкции, с которой начинается выполнение программы.
- Этот адрес добавляется к базовому адресу исполняемого файла (`ImageBase`) для получения полного виртуального адреса точки входа.
- Используется для инициализации регистра `IP` (или `EIP` на x86, `RIP` на x64).

#### `ImageBase`
- Базовый адрес, по которому загружается образ PE-файла в виртуальную память.
- Загрузчик ОС добавляет смещение из `AddressOfEntryPoint` к этому значению, чтобы вычислить начальное значение регистра `IP`.

#### `BaseOfCode`
- Указывает начальный виртуальный адрес секции `.text`, где находится исполняемый код.
- Используется загрузчиком для настройки адресов секций, но не влияет непосредственно на `IP`.

#### `SizeOfCode`
- Указывает общий размер секций, содержащих исполняемый код (например, `.text`).
- Это справочная информация для загрузчика и не влияет непосредственно на инициализацию регистров.

#### `SizeOfInitializedData`
- Указывает размер данных, которые инициализированы в секциях, таких как `.data`.
- Влияет на структуру образа в памяти, но не на начальное значение регистров.

#### `SizeOfUninitializedData`
- Размер данных, которые не инициализированы (например, секция `.bss`).
- Это поле используется загрузчиком для выделения памяти, но не влияет на начальные регистры.

---

### 2. Инициализация регистров

#### Регистр `IP` (Instruction Pointer)
- Значение `IP` = `ImageBase` + `AddressOfEntryPoint`.
- Определяет начальный адрес инструкции, с которой начнётся выполнение.

#### Регистр `CS` (Code Segment)
- В современных ОС (например, Windows NT и выше) используется модель Flat Memory Model, где все сегментные регистры указывают на один и тот же базовый адрес.
- `CS` и другие сегментные регистры настраиваются ОС так, чтобы указывать на адрес пространства приложения, заданный `ImageBase`.

---

### 3. Пример вычисления
Предположим:
- `ImageBase = 0x00400000`  
- `AddressOfEntryPoint = 0x00001000`  

В этом случае:
- Начальный адрес инструкции = `0x00400000 + 0x00001000 = 0x00401000`.  
- Регистр `IP` будет равен `0x00401000`.

Регистр `CS` в модели Flat Memory Model просто устанавливается ОС, чтобы корректно интерпретировать адрес.

---

### 4. Как загрузчик обрабатывает эти поля
1. Загрузчик ОС читает `ImageBase` и загружает PE-файл в указанное виртуальное адресное пространство.
2. Находит `AddressOfEntryPoint` и вычисляет виртуальный адрес начала выполнения.
3. Устанавливает регистр `IP` (или `RIP` на x64) для начала выполнения программы.
4. Устанавливает `CS` для корректного адресного пространства (Flat Memory Model).

Если есть необходимость в практическом анализе этих полей (например, с использованием Python и библиотеки `pefile`), я могу подготовить пример.
---

#### 4. Data Directories
В Optional Header есть таблица, называемая **Data Directory**, содержащая указатели на важные таблицы данных программы.

| Поле                   | Назначение                                                                 |
|------------------------|---------------------------------------------------------------------------|
| `Export Table`         | Экспортируемые функции, если файл является библиотекой (DLL).             |
| `Import Table`         | Импортируемые функции (зависимости от других DLL).                        |
| `Resource Table`       | Ресурсы программы (иконки, строки, изображения).                          |
| `Exception Table`      | Таблица обработки исключений.                                             |
| `Relocation Table`     | Информация для переноса образа в памяти, если базовый адрес занят.         |
| `Debug`                | Отладочная информация.                                                   |

---

### Таблица секций (Section Table)
После PE-заголовка следует таблица секций. Она описывает содержимое файла (например, код, данные или ресурсы).

| Поле          | Размер  | Описание                                                                 |
|---------------|---------|-------------------------------------------------------------------------|
| `Name`        | 8 байт  | Имя секции (например, `.text`, `.data`, `.rsrc`).                       |
| `VirtualSize` | 4 байта | Размер секции в памяти.                                                 |
| `VirtualAddress` | 4 байта | Адрес секции в виртуальной памяти (RVA).                              |
| `SizeOfRawData` | 4 байта | Размер секции в файле.                                                |
| `PointerToRawData` | 4 байта | Смещение секции в файле.                                           |
| `Characteristics` | 4 байта | Флаги доступа (например, только чтение или выполнение).              |

---

### Ключевые поля для выполнения программы
1. **AddressOfEntryPoint** — указывает точку входа, с которой начинается выполнение кода.
2. **ImageBase** — базовый адрес загрузки программы в память.
3. **Section Table** — описывает, как размещать код и данные в памяти.

PE-заголовок является универсальным и поддерживает как EXE-файлы, так и DLL, что делает его основой для всех программ в Windows.


-------------------------

Характеристики PE-файла, указанные в **File Header** (поле `Characteristics`), представляют собой набор флагов, описывающих свойства и особенности файла. Это 16-битное значение (2 байта), где каждый бит отвечает за конкретную характеристику.

---

### Структура поля Characteristics
Поле `Characteristics` находится в **IMAGE_FILE_HEADER** и используется для описания поведения исполняемого файла или библиотеки. Его значения задаются с помощью набора битовых флагов.

---

### Возможные флаги (биты) в `Characteristics`
| Флаг               | Значение (бит) | Описание                                                                                     |
|------------------------|-------------------|-------------------------------------------------------------------------------------------------|
| `IMAGE_FILE_RELOCS_STRIPPED`       | `0x0001`          | Удалена таблица релокаций (релокации не поддерживаются).                                         |
| `IMAGE_FILE_EXECUTABLE_IMAGE`      | `0x0002`          | Файл является исполняемым.                                                                      |
| `IMAGE_FILE_LINE_NUMS_STRIPPED`    | `0x0004`          | Удалена информация о номерах строк (обычно используется для отладки).                           |
| `IMAGE_FILE_LOCAL_SYMS_STRIPPED`   | `0x0008`          | Удалены локальные символы (используется для уменьшения размера файла).                          |
| `IMAGE_FILE_AGGRESSIVE_WS_TRIM`    | `0x0010`          | Агрессивное удаление страниц из рабочего набора (deprecated).                                   |
| `IMAGE_FILE_LARGE_ADDRESS_AWARE`   | `0x0020`          | Программа поддерживает адреса более 2 ГБ (на 32-битных системах) или 4 ГБ (на 64-битных).       |
| `IMAGE_FILE_BYTES_REVERSED_LO`     | `0x0080`          | Устарело. Лоу-байты слов хранятся в обратном порядке (big-endian).                              |
| `IMAGE_FILE_32BIT_MACHINE`         | `0x0100`          | Файл предназначен для 32-битной машины (не 16-битной).                                          |
| `IMAGE_FILE_DEBUG_STRIPPED`        | `0x0200`          | Удалена отладочная информация.                                                                 |
| `IMAGE_FILE_REMOVABLE_RUN_FROM_SWAP`| `0x0400`          | Загрузка файла в память при запуске с съемного носителя.                                        |
| `IMAGE_FILE_NET_RUN_FROM_SWAP`     | `0x0800`          | Загрузка файла в память при запуске по сети.                                                   |
| `IMAGE_FILE_SYSTEM`                | `0x1000`          | Файл является системным (драйвер или системная библиотека).                                     |
| `IMAGE_FILE_DLL`                   | `0x2000`          | Файл является библиотекой (DLL).                                                               |
| `IMAGE_FILE_UP_SYSTEM_ONLY`        | `0x4000`          | Файл может выполняться только на однопроцессорной системе.                                      |
| `IMAGE_FILE_BYTES_REVERSED_HI`     | `0x8000`          | Устарело. Хай-байты слов хранятся в обратном порядке (big-endian).                              |

---

### Расшифровка типичных значений
- **Исполняемый файл (EXE):**
  - `0x0002 | 0x0100 = 0x0102`:  
    - `IMAGE_FILE_EXECUTABLE_IMAGE`: Файл исполняемый.  
    - `IMAGE_FILE_32BIT_MACHINE`: Для 32-битной архитектуры.  

- **Библиотека (DLL):**
  - `0x2000 | 0x0100 = 0x2100`:  
    - `IMAGE_FILE_DLL`: Файл является DLL.  
    - `IMAGE_FILE_32BIT_MACHINE`: Для 32-битной архитектуры.  

- **64-битное приложение:**
  - `0x0020 | 0x0100 = 0x0120`:  
    - `IMAGE_FILE_LARGE_ADDRESS_AWARE`: Поддерживает адреса более 2 ГБ.  
    - `IMAGE_FILE_32BIT_MACHINE`: Поддержка современных архитектур.

---

### Использование в системах Windows
1. **Операционная система**: Использует флаги для принятия решений о загрузке и выполнении файла. Например:
   - Если установлен `IMAGE_FILE_DLL`, файл загружается как библиотека, а не как исполняемый файл.
   - Если указан `IMAGE_FILE_LARGE_ADDRESS_AWARE`, файл сможет использовать больше виртуальной памяти на 64-битной системе.
   
2. **Среды разработки и отладки**: Инструменты могут удалять или модифицировать некоторые флаги (например, `IMAGE_FILE_DEBUG_STRIPPED`) для уменьшения размера файла или ускорения работы.

3. **Оптимизация для конкретных платформ**: Например, флаг `IMAGE_FILE_UP_SYSTEM_ONLY` может быть установлен для драйверов, если они рассчитаны только на однопроцессорные системы.

---

### Примечания
- Некоторые флаги, такие как `IMAGE_FILE_BYTES_REVERSED_LO` и `IMAGE_FILE_BYTES_REVERSED_HI`, устарели и не используются в современных PE-файлах.  
- Характеристики файла могут быть изменены компоновщиком во время сборки или специально модифицированы утилитами (например, **CFF Explorer**).

Поле `Characteristics` играет важную роль в обеспечении совместимости и корректной работы PE-файлов на целевых системах.

-------------------------

    00A0  50 45 00 00 4C 01 03 00 C3 40 C6 66 00 00 00 00  PE..L....@.f.... 
    00B0  00 00 00 00 E0 00 03 01 0B 01 01 00 00 02 00 00  ................ 
    00C0  00 04 00 00 00 00 00 00 00 10 00 00 00 10 00 00  ................ 
    00D0  00 20 00 00 00 00 40 00 00 10 00 00 00 02 00 00  . ....@......... 
    00E0  04 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00  ................ 
    00F0  00 40 00 00 00 04 00 00 8D 24 00 00 02 00 00 00  .@.......$...... 
    0100  00 00 10 00 00 00 01 00 00 00 10 00 00 10 00 00  ................ 
    0110  00 00 00 00 10 00 00 00 00 00 00 00 00 00 00 00  ................ 
    0120  1C 30 00 00 3C 00 00 00 00 00 00 00 00 00 00 00  .0..<........... 
    0130  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................ 
    0140  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................ 
    0150  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................ 
    0160  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................ 
    0170  00 00 00 00 00 00 00 00 00 30 00 00 10 00 00 00  .........0...... 
    0180  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................ 
    0190  00 00 00 00 00 00 00 00                          ........


| field | value | start | size | type | color | comment |
|----|----|----|----|----|----|----|
| NtHeader | | A0h | F8h | struct IMAGE_NT_HEADERS | Fg: Bg:0x2E8D8D | 
| &nbsp; Signature | 4550h | A0h | 4h | DWORD | Fg: Bg:0x2E8D8D | IMAGE_NT_SIGNATURE = 0x00004550
| &nbsp; FileHeader |  | A4h | 14h | struct IMAGE_FILE_HEADER | Fg: Bg:0x2E8D8D | 
| &nbsp; &nbsp; Machine | I386 (14Ch) | A4h | 2h | enum IMAGE_MACHINE | Fg:0xFF00FF Bg:0x2E8D8D | WORD
| &nbsp; &nbsp; NumberOfSections | 3 | A6h | 2h | WORD | Fg:0xFF0000 Bg:0x2E8D8D | Section num
| &nbsp; &nbsp; TimeDateStamp | 08/21/2024 19:32:19 | A8h | 4h | time_t | Fg: Bg:0x2E8D8D | DWORD,from 01/01/1970 12:00 AM
| &nbsp; &nbsp; PointerToSymbolTable | 0h | ACh | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; &nbsp; NumberOfSymbols | 0 | B0h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; &nbsp; SizeOfOptionalHeader | E0h | B4h | 2h | WORD | Fg: Bg:0x2E8D8D | 
| &nbsp; &nbsp; Characteristics |  | B6h | 2h | struct FILE_CHARACTERISTICS | Fg: Bg:0x2E8D8D | WORD
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_RELOCS_STRIPPED : 1 | 1 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0001  Relocation info stripped from file
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_EXECUTABLE_IMAGE : 1 | 1 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0002  File is executable
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_LINE_NUMS_STRIPPED : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0004  Line nunbers stripped from file
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_LOCAL_SYMS_STRIPPED : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0008  Local symbols stripped from file
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_AGGRESIVE_WS_TRIM : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0010  Agressively trim working set
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_LARGE_ADDRESS_AWARE : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0020  App can handle >2gb addresses
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_BYTES_REVERSED_LO : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0080  Bytes of machine word are reversed
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_32BIT_MACHINE : 1 | 1 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0100  32 bit word machine
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_DEBUG_STRIPPED : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0200  Debugging info stripped from file in .DBG file
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_REMOVABLE_RUN_FROM_SWAP : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0400  If Image is on removable media, copy and run from the swap file
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_NET_RUN_FROM_SWAP : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0800  If Image is on Net, copy and run from the swap file
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_SYSTEM : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x1000  System File
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_DLL : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x2000  File is a DLL
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_UP_SYSTEM_ONLY : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x4000  File should only be run on a UP machine
| &nbsp; &nbsp; &nbsp; IMAGE_FILE_BYTES_REVERSED_HI : 1 | 0 | B6h | 2h | WORD | Fg: Bg:0x2E8D8D | 0x8000  Bytes of machine word are reversed
| &nbsp; OptionalHeaderMagic | 267 | (local) |  | WORD |  | 
| &nbsp; OptionalHeader |  | B8h | E0h | struct IMAGE_OPTIONAL_HEADER32 | Fg: Bg:0x2E8D8D | 
| &nbsp; Magic | PE32 (10Bh) | B8h | 2h | enum OPTIONAL_MAGIC | Fg: Bg:0x2E8D8D | WORD
| &nbsp; MajorLinkerVersion | 1 | BAh | 1h | BYTE | Fg: Bg:0x2E8D8D | 
| &nbsp; MinorLinkerVersion | 0 | BBh | 1h | BYTE | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfCode | 200h | BCh | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfInitializedData | 400h | C0h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfUninitializedData | 0h | C4h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; AddressOfEntryPoint | 1000h | C8h | 4h | DWORD | Fg:0xFF00FF Bg:0x2E8D8D | .text FOA = 0x400 
| &nbsp; BaseOfCode | 1000h | CCh | 4h | DWORD | Fg: Bg:0x2E8D8D | .text FOA = 0x400 
| &nbsp; BaseOfData | 2000h | D0h | 4h | DWORD | Fg: Bg:0x2E8D8D | .data FOA = 0x600 
| &nbsp; ImageBase | 400000h | D4h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SectionAlignment | 1000h | D8h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; FileAlignment | 200h | DCh | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; MajorOperatingSystemVersion | 4 | E0h | 2h | WORD | Fg: Bg:0x2E8D8D | 
| &nbsp; MinorOperatingSystemVersion | 0 | E2h | 2h | WORD | Fg: Bg:0x2E8D8D | 
| &nbsp; MajorImageVersion | 0 | E4h | 2h | WORD | Fg: Bg:0x2E8D8D | 
| &nbsp; MinorImageVersion | 0 | E6h | 2h | WORD | Fg: Bg:0x2E8D8D | 
| &nbsp; MajorSubsystemVersion | 4 | E8h | 2h | WORD | Fg: Bg:0x2E8D8D | 
| &nbsp; MinorSubsystemVersion | 0 | EAh | 2h | WORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Win32VersionValue | 0 | ECh | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfImage | 4000h | F0h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfHeaders | 400h | F4h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; CheckSum | 248Dh | F8h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Subsystem | WINDOWS_GUI (2) | FCh | 2h | enum IMAGE_SUBSYSTEM | Fg: Bg:0x2E8D8D | WORD
| &nbsp; DllCharacteristics |  | FEh | 2h | struct DLL_CHARACTERISTICS | Fg: Bg:0x2E8D8D | WORD
| &nbsp; IMAGE_LIBRARY_PROCESS_INIT : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0001 Reserved
| &nbsp; IMAGE_LIBRARY_PROCESS_TERM : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0002 Reserved
| &nbsp; IMAGE_LIBRARY_THREAD_INIT : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0004 Reserved
| &nbsp; IMAGE_LIBRARY_THREAD_TERM : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0008 Reserved
| &nbsp; IMAGE_DLLCHARACTERISTICS_HIGH_ENTROPY_VA : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0020
| &nbsp; IMAGE_DLLCHARACTERISTICS_DYNAMIC_BASE : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0040
| &nbsp; IMAGE_DLLCHARACTERISTICS_FORCE_INTEGRITY : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0080
| &nbsp; IMAGE_DLLCHARACTERISTICS_NX_COMPAT : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0100
| &nbsp; IMAGE_DLLCHARACTERISTICS_NO_ISOLATION : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0200
| &nbsp; IMAGE_DLLCHARACTERISTICS_NO_SEH : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0400
| &nbsp; IMAGE_DLLCHARACTERISTICS_NO_BIND : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x0800
| &nbsp; IMAGE_DLLCHARACTERISTICS_WDM_DRIVER : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x2000
| &nbsp; IMAGE_DLLCHARACTERISTICS_TERMINAL_SERVER_AWARE : 1 | 0 | FEh | 2h | WORD | Fg: Bg:0x2E8D8D | 0x8000
| &nbsp; SizeOfStackReserve | 100000h | 100h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfStackCommit | 10000h | 104h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfHeapReserve | 100000h | 108h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; SizeOfHeapCommit | 1000h | 10Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; LoaderFlags | 0 | 110h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; NumberOfRvaAndSizes | 16 | 114h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; DataDirArray |  | 118h | 80h | struct IMAGE_DATA_DIRECTORY_ARRAY | Fg: Bg:0x2E8D8D | 
| &nbsp; len | 16 | (local) |  | int |  | 
| &nbsp; Export |  | 118h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_EXPORT
| &nbsp; VirtualAddress | 0h | 118h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 11Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Import | 0000301C: size = 60 | 120h | 8h | struct IMAGE_DATA_DIRECTORY | Fg:0xFF00FF Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_IMPORT
| &nbsp; VirtualAddress | 301Ch | 120h | 4h | DWORD | Fg:0xFF00FF Bg:0x2E8D8D | .idata FOA = 0x81C 
| &nbsp; Size | 3Ch | 124h | 4h | DWORD | Fg:0xFF00FF Bg:0x2E8D8D | 
| &nbsp; Resource |  | 128h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_RESOURCE
| &nbsp; VirtualAddress | 0h | 128h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 12Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Exception |  | 130h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_EXCEPTION
| &nbsp; VirtualAddress | 0h | 130h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 134h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Security |  | 138h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_SECURITY
| &nbsp; VirtualAddress | 0h | 138h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 13Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; BaseRelocationTable |  | 140h | 8h | struct IMAGE_DATA_DIRECTORY | Fg:0xFF00FF Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_BASERELOC
| &nbsp; VirtualAddress | 0h | 140h | 4h | DWORD | Fg:0xFF00FF Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 144h | 4h | DWORD | Fg:0xFF00FF Bg:0x2E8D8D | 
| &nbsp; DebugDirectory |  | 148h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_DEBUG
| &nbsp; VirtualAddress | 0h | 148h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 14Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; CopyrightOrArchitectureSpecificData |  | 150h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_ARCHITECTURE
| &nbsp; VirtualAddress | 0h | 150h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 154h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; GlobalPtr |  | 158h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_GLOBALPTR
| &nbsp; VirtualAddress | 0h | 158h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 15Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; TLSDirectory |  | 160h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_TLS
| &nbsp; VirtualAddress | 0h | 160h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 164h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; LoadConfigurationDirectory |  | 168h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_LOAD_CONFIG
| &nbsp; VirtualAddress | 0h | 168h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 16Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; BoundImportDirectory |  | 170h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_BOUND_IMPORT
| &nbsp; VirtualAddress | 0h | 170h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 174h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; ImportAddressTable | 00003000: size = 16 | 178h | 8h | struct IMAGE_DATA_DIRECTORY | Fg:0xFF00FF Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_IAT
| &nbsp; VirtualAddress | 3000h | 178h | 4h | DWORD | Fg:0xFF00FF Bg:0x2E8D8D | .idata FOA = 0x800 
| &nbsp; Size | 10h | 17Ch | 4h | DWORD | Fg:0xFF00FF Bg:0x2E8D8D | 
| &nbsp; DelayLoadImportDescriptors |  | 180h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_DELAY_IMPORT
| &nbsp; VirtualAddress | 0h | 180h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 184h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; CLRDirectory |  | 188h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | IMAGE_DIRECTORY_ENTRY_CLR
| &nbsp; VirtualAddress | 0h | 188h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 18Ch | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Reserved |  | 190h | 8h | struct IMAGE_DATA_DIRECTORY | Fg: Bg:0x2E8D8D | System Reserved
| &nbsp; VirtualAddress | 0h | 190h | 4h | DWORD | Fg: Bg:0x2E8D8D | 
| &nbsp; Size | 0h | 194h | 4h | DWORD | Fg: Bg:0x2E8D8D | 