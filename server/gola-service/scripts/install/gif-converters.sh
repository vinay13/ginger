git clone https://github.com/pornel/giflossy.git
cd giflossy
sudo apt install autoconf
autoreconf -i
sudo apt-get install build-essential
./configure
sudo make install
sudo apt-get install imagemagick