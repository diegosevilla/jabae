Program: 
	nasm -f elf64 -o try.o test.asm && ld -o try try.o && ./try
