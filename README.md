# TCMCidadeIluminada

## Instaçacao de ambiente no Ubuntu 

- sudo apt-get install python-pip python-dev build-essential git sqlite3
- $ sudo pip install --upgrade pip 
- $ sudo pip install --upgrade virtualenv 
- $ sudo pip install virtualenvwrapper

## Baixando e Instalando aplicacao

- $ git clone https://github.com/HardDiskD/TCMCidadeIluminada
- $ cd TCMCidadeIluminada
- $ pip install -r requirement.txt
- $ python manage.py db upgrade
- $ python manage.py runserver
- ir para http://localhost:5000/protocolos
