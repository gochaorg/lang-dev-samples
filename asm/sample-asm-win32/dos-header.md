```
0000  4D 5A 90 00 03 00 00 00 04 00 00 00 FF FF 00 00  MZ..........ÿÿ.. 
0010  B8 00 00 00 00 00 00 00 40 00 00 00 00 00 00 00  ¸.......@....... 
0020  00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00  ................ 
0030  00 00 00 00 00 00 00 00 00 00 00 00 A0 00 00 00  ............ ... 
```

```c
typedef struct
{
    WORD   MZSignature              <comment="IMAGE_DOS_SIGNATURE = 0x5A4D",format=hex,style=sHeading1Accent>;
    WORD   UsedBytesInTheLastPage   <comment="Bytes on last page of file">;
    WORD   FileSizeInPages          <comment="Pages in file">;
    WORD   NumberOfRelocationItems  <comment="Relocations">;
    WORD   HeaderSizeInParagraphs   <comment="Size of header in paragraphs">;
    WORD   MinimumExtraParagraphs   <comment="Minimum extra paragraphs needed">;
    WORD   MaximumExtraParagraphs   <comment="Maximum extra paragraphs needed">;
    WORD   InitialRelativeSS        <comment="Initial (relative) SS value">;
    WORD   InitialSP                <comment="Initial SP value">;
    WORD   Checksum                 <comment="Checksum">;
    WORD   InitialIP                <comment="Initial IP value">;
    WORD   InitialRelativeCS        <comment="Initial (relative) CS value">;
    WORD   AddressOfRelocationTable <comment="File address of relocation table">;
    WORD   OverlayNumber            <comment="Overlay number">;
    WORD   Reserved[4]              <comment="Reserved words">;
    WORD   OEMid                    <comment="OEM identifier (for OEMinfo)">;
    WORD   OEMinfo                  <comment="OEM information; OEMid specific">;
    WORD   Reserved2[10]            <comment="Reserved words">;
    LONG   AddressOfNewExeHeader    <comment="NtHeader Offset",format=hex>;
} IMAGE_DOS_HEADER <style=sHeading1>;
```

    MZSignature	5A4Dh	0h	2h	WORD	Fg: Bg:0x5E48A6	IMAGE_DOS_SIGNATURE = 0x5A4D
    UsedBytesInTheLastPage	144	2h	2h	WORD	Fg: Bg:0x462D99	Bytes on last page of file
    FileSizeInPages	3	4h	2h	WORD	Fg: Bg:0x462D99	Pages in file
    NumberOfRelocationItems	0	6h	2h	WORD	Fg: Bg:0x462D99	Relocations
    HeaderSizeInParagraphs	4	8h	2h	WORD	Fg: Bg:0x462D99	Size of header in paragraphs
    MinimumExtraParagraphs	0	Ah	2h	WORD	Fg: Bg:0x462D99	Minimum extra paragraphs needed
    MaximumExtraParagraphs	65535	Ch	2h	WORD	Fg: Bg:0x462D99	Maximum extra paragraphs needed
    InitialRelativeSS	0	Eh	2h	WORD	Fg: Bg:0x462D99	Initial (relative) SS value
    InitialSP	184	10h	2h	WORD	Fg: Bg:0x462D99	Initial SP value
    Checksum	0	12h	2h	WORD	Fg: Bg:0x462D99	Checksum
    InitialIP	0	14h	2h	WORD	Fg: Bg:0x462D99	Initial IP value
    InitialRelativeCS	0	16h	2h	WORD	Fg: Bg:0x462D99	Initial (relative) CS value
    AddressOfRelocationTable	64	18h	2h	WORD	Fg: Bg:0x462D99	File address of relocation table
    OverlayNumber	0	1Ah	2h	WORD	Fg: Bg:0x462D99	Overlay number
    OEMid	0	24h	2h	WORD	Fg: Bg:0x462D99	OEM identifier (for OEMinfo)
    OEMinfo	0	26h	2h	WORD	Fg: Bg:0x462D99	OEM information; OEMid specific
    AddressOfNewExeHeader	A0h	3Ch	4h	LONG	Fg: Bg:0x462D99	NtHeader Offset

**DOS Header** (или MS-DOS заголовок) — это первая часть EXE-файла, которая предназначена для обеспечения совместимости с MS-DOS. Она занимает первые 64 байта файла и имеет фиксированную структуру.

---

### Структура DOS Header (gpt)
DOS Header представлен в формате C-структуры, например, так:

```c
typedef struct _IMAGE_DOS_HEADER {
    WORD e_magic;    // Сигнатура, всегда "MZ" (0x4D5A)
    WORD e_cblp;     // Количество байт в последней странице файла
    WORD e_cp;       // Количество страниц файла
    WORD e_crlc;     // Количество записей в таблице переадресации
    WORD e_cparhdr;  // Размер заголовка в параграфах
    WORD e_minalloc; // Минимальное количество параграфов памяти
    WORD e_maxalloc; // Максимальное количество параграфов памяти
    WORD e_ss;       // Начальное значение регистра SS
    WORD e_sp;       // Начальное значение регистра SP
    WORD e_csum;     // Контрольная сумма
    WORD e_ip;       // Начальное значение регистра IP
    WORD e_cs;       // Начальное значение регистра CS
    WORD e_lfarlc;   // Смещение таблицы переадресации
    WORD e_ovno;     // Номер оверлея
    WORD e_res[4];   // Зарезервировано (0)
    WORD e_oemid;    // Идентификатор OEM
    WORD e_oeminfo;  // Информация OEM
    WORD e_res2[10]; // Зарезервировано (0)
    DWORD e_lfanew;  // Смещение до PE-заголовка (IMAGE_NT_HEADERS)
} IMAGE_DOS_HEADER;
```

---

### Поле **e_lfanew**
- Поле `e_lfanew` (4 байта) указывает на **смещение до PE-заголовка** (IMAGE_NT_HEADERS) от начала файла.  
- Это значение позволяет операционной системе найти начало структуры PE, даже если перед ним есть дополнительные данные (например, DOS-Stub или резервированные области).

---

### Пример
Если `e_lfanew = 0x00000080`, то PE-заголовок находится на **80-й байте** файла. Это позволяет ОС пропустить DOS-совместимые данные и сразу начать обработку в формате Portable Executable.

---

### Назначение DOS-заголовка
1. **Совместимость с MS-DOS:** Если файл пытаются запустить в DOS, будет выполнена Stub-программа, которая обычно выводит сообщение `This program cannot be run in DOS mode.`
2. **Навигация в PE-структуре:** Поле `e_lfanew` помогает найти начало основной структуры PE-файла.

---

DOS Header — это неотъемлемая часть всех EXE-файлов, даже если они никогда не будут запускаться в DOS-среде.

---

В Windows 10, как и в других версиях Windows, DOS-заголовок (DOS Header) используется преимущественно для совместимости и указания пути к PE-заголовку. Большинство полей DOS Header не играет активной роли в современных системах, однако они должны быть корректно заполнены, чтобы файл распознавался как исполняемый. Рассмотрим значения, которые типично встречаются в DOS Header для современных EXE-файлов:

---

### Типичные значения полей DOS Header в Windows 10
| **Поле**       | **Типичное значение**             | **Описание**                                                                                     |
|-----------------|-----------------------------------|-------------------------------------------------------------------------------------------------|
| `e_magic`      | `0x5A4D` ("MZ")                  | Обязательная сигнатура файла. Указывает, что это DOS-заголовок.                                  |
| `e_cblp`       | `0x0090`                         | Количество байт в последней странице файла. Обычно 144.                                         |
| `e_cp`         | `0x0003`                         | Количество страниц в файле. Обычно 3 страницы.                                                  |
| `e_crlc`       | `0x0000`                         | Количество записей в таблице переадресации. Современные файлы обычно не используют это поле.    |
| `e_cparhdr`    | `0x0004`                         | Размер заголовка в параграфах.                                                                 |
| `e_minalloc`   | `0x0000`                         | Минимальное количество параграфов памяти.                                                      |
| `e_maxalloc`   | `0xFFFF`                         | Максимальное количество параграфов памяти.                                                     |
| `e_ss`         | `0x0000`                         | Начальное значение регистра SS.                                                                |
| `e_sp`         | `0x00B8`                         | Начальное значение регистра SP.                                                                |
| `e_csum`       | `0x0000`                         | Контрольная сумма файла. Обычно не используется, оставляется равной 0.                         |
| `e_ip`         | `0x0000`                         | Начальное значение регистра IP.                                                                |
| `e_cs`         | `0x0000`                         | Начальное значение регистра CS.                                                                |
| `e_lfarlc`     | `0x0040`                         | Смещение таблицы переадресации. Указывает на 64-й байт.                                         |
| `e_ovno`       | `0x0000`                         | Номер оверлея. Обычно 0.                                                                        |
| `e_res`        | Все нули (`0x0000`)              | Зарезервировано, не используется.                                                              |
| `e_oemid`      | `0x0000`                         | Идентификатор OEM. Оставляется равным 0.                                                       |
| `e_oeminfo`    | `0x0000`                         | Информация OEM. Оставляется равной 0.                                                          |
| `e_res2`       | Все нули (`0x0000`)              | Зарезервировано, не используется.                                                              |
| `e_lfanew`     | Смещение до PE-заголовка         | Обычно указывает на смещение 128 байт (`0x00000080`) или больше, в зависимости от размера DOS Stub. |

---

### Поле, играющее ключевую роль: **e_lfanew**
- В Windows 10 **e_lfanew** является самым важным значением в DOS Header.  
- Оно указывает на начало PE-заголовка, где начинается современная структура файла (PE/COFF).

### Примечание
Поля DOS-заголовка практически не используются в Windows 10 для выполнения кода, кроме тех, которые необходимы для распознавания формата файла. Основная часть заголовка нужна для совместимости и правильной обработки файла загрузчиком ОС.