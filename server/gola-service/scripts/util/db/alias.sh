alias dbclient='mongo 172.31.11.146:7273/gingerdb -u gingeradmin -p --authenticationDatabase=gingerdb'


alias dbrestart='sudo systemctl restart mongod.service'
alias dbstatus='sudo systemctl status  mongod.service'
alias dbstop='sudo systemctl stop  mongod.service'

