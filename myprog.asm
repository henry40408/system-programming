         START
    TEMP RESW 1
  TEMP_2 RESW 1
     AAA WORD 1
  TEMP_1 RESW 1
     CCC WORD 3
     BBB WORD 2
     EEE WORD 5
     DDD WORD 444
     FFF RESW 1
  TEMP_4 RESW 1
  TEMP_3 RESW 1
         MOV  BBB, AX
         MOV  AAA, BX
         MUL  BX, AX
         MOV  AX, TEMP_1
         MOV  DDD, AX
         MOV  CCC, BX
         DIV  BX, AX
         MOV  AX, TEMP_2
         MOV  TEMP_2, AX
         MOV  TEMP_1, BX
         ADD  BX, AX
         MOV  AX, TEMP_3
         MOV  EEE, AX
         MOV  TEMP_3, BX
         SUB  BX, AX
         MOV  AX, TEMP_4
         MOV  TEMP_4, AX
         MOV  AX, FFF
         WRT  FFF
         END
