    0040  0E 1F BA 0E 00 B4 09 CD 21 B8 01 4C CD 21 54 68  ..º..´.Í!¸.LÍ!Th 
    0050  69 73 20 70 72 6F 67 72 61 6D 20 63 61 6E 6E 6F  is program canno 
    0060  74 20 62 65 20 72 75 6E 20 69 6E 20 44 4F 53 20  t be run in DOS  
    0070  6D 6F 64 65 2E 0D 0D 0A 24 00 00 00 00 00 00 00  mode....$....... 
    0080  47 6F 4C 69 6E 6B 20 77 77 77 2E 47 6F 44 65 76  GoLink www.GoDev 
    0090  54 6F 6F 6C 2E 63 6F 6D 00 00 00 00 00 00 00 00  Tool.com........ 


**DOS Stub** — это небольшой участок кода, который находится в PE-файле (EXE или DLL) сразу после DOS Header и выполняется, если попытаться запустить PE-файл в среде MS-DOS. Этот код был добавлен для совместимости и исторических целей.

---

### Основные функции DOS Stub:
1. **Совместимость с DOS:** Если PE-файл запускается в DOS-среде, вместо исполнения современного кода выводится сообщение или выполняется простая программа.
2. **Сообщение о невозможности выполнения:** Чаще всего стандартный DOS Stub выводит сообщение:
   ```
   This program cannot be run in DOS mode.
   ```
3. **Размер DOS Stub:** Типичный размер DOS Stub — 64 байта или чуть больше.

---

### Расположение DOS Stub
- DOS Stub располагается сразу после DOS Header и заканчивается на смещении, указанном в поле `e_lfanew` (смещение до PE-заголовка).
- Код DOS Stub интерпретируется DOS-системой как обычный DOS-исполняемый файл.

---

### Структура DOS Stub
Простейший DOS Stub может быть записан на языке ассемблера. Пример кода:

```assembly
mov ah, 9             ; Вызов функции вывода строки в DOS
mov dx, offset msg    ; Адрес строки
int 21h               ; Прерывание DOS
int 20h               ; Завершение программы
msg db "This program cannot be run in DOS mode.$"
```

Этот код:
- Выводит сообщение.
- Завершает выполнение программы в DOS.

---

### Реальное содержимое DOS Stub
Стандартный DOS Stub, генерируемый компоновщиками (например, Microsoft Visual Studio), обычно имеет следующую структуру в шестнадцатеричном представлении:

```plaintext
B8 01 4C CD 21 This program cannot be run in DOS mode.$
```

Этот код:
- Использует функцию DOS для завершения программы (`INT 21h` с кодом возврата).

---

### Размер и настройки
- **Минимальный размер** DOS Stub — всего несколько байт, чтобы поместить сообщение.
- **Кастомизация:** В некоторых случаях разработчики могут заменить стандартный DOS Stub своим кодом (например, загрузчиком или отображением других сообщений).

---

### Использование в современных системах
DOS Stub практически не используется в современных системах, так как PE-файлы запускаются в Windows-среде, а DOS практически не используется. Тем не менее, он является обязательной частью формата PE и добавляется во все EXE-файлы.

---

DOS Stub — это скорее исторический рудимент, но его наличие в PE-файлах сохраняет совместимость и соблюдает спецификацию формата.