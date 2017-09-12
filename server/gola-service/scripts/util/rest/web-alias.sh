alias restlogs='cd /opt/software/gola/prod/logs;tail -100f `ls -Art | tail -n 1`'
alias restrestart='sudo systemctl restart golaweb.service'
alias reststatus='sudo systemctl status  golaweb.service'
alias reststop='sudo systemctl stop golaweb.service'