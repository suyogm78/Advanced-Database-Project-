CREATE  DATABASE LINK DistrDB  CONNECT TO
    "spm122@toolman"
    IDENTIFIED BY "dz19oLOt"
    USING '(DESCRIPTION = (ADDRESS_LIST = (ADDRESS = (PROTOCOL =
    TCP) (Host = oracle1.wiu.edu) (PORT = 1521))) (CONNECT_DATA = (SID
    = toolman )))'
/
