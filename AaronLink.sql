CREATE  DATABASE LINK DistrDB  CONNECT TO
    "amb143@toolman"
    IDENTIFIED BY "as75nTDd"
    USING '(DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (PROTOCOL =
    TCP) (Host = oracle1.wiu.edu) (PORT = 1521))) (CONNECT_DATA = (SID
    = toolman )))'
/
