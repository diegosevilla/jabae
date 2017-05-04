section .data
	data0 db "asdasd",10

section .bss

section .text
	global _start

_start:

	mov rax, 1
	mov rdi, 1
	mov rsi, data0
	mov rdx, 7
	syscall

	mov rax, 60
	mov rdi, 0
	syscall
