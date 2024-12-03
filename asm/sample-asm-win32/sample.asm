section .data
    msg db 'Hello, World!', 0
    caption db 'Message', 0

section .text
    extern MessageBoxA
    extern ExitProcess

    global _start

_start:
    ; hWnd = NULL (0)
    push 0

    ; Text of the message
    push msg

    ; Caption of the message box
    push caption

    ; MB_OK flag
    push 0

    ; Call MessageBoxA
    call MessageBoxA

    ; Exit process
    push 0
    call ExitProcess
