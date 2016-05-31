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
         MOV  AAA, AX
         MOV  BBB, BX
         MUL  AX, BX
         MOV  BX, TEMP_1
         MOV  CCC, AX
         MOV  DDD, BX
         DIV  AX, BX
         MOV  BX, TEMP_2
         MOV  TEMP_1, AX
         MOV  TEMP_2, BX
         ADD  AX, BX
         MOV  BX, TEMP_3
         MOV  TEMP_3, AX
         MOV  EEE, BX
         SUB  AX, BX
         MOV  BX, TEMP_4
         MOV  TEMP_4, AX
         MOV  AX, FFF
         WRT  FFF
         END
