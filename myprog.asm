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
         MUL  BBB, AX
         MOV  AX, TEMP_1
         MOV  CCC, AX
         DIV  DDD, AX
         MOV  AX, TEMP_2
         MOV  TEMP_1, AX
         ADD  TEMP_2, AX
         MOV  AX, TEMP_3
         MOV  TEMP_3, AX
         SUB  EEE, AX
         MOV  AX, TEMP_4
         MOV  TEMP_4, AX
         MOV  AX, FFF
         WRT  FFF
         END
