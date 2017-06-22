import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { HomeService } from '../../services/home.service';

@Component({
  selector: 'page-page2',
  templateUrl: 'page2.html'
})
export class Page2Page {
    // celebrity page

    constructor(public navCtrl: NavController, 
                public navParams: NavParams,
                public _homeserv : HomeService) {
                  this.getTrendingGIFs();
                 }


    public trendingGIFs: any;
    public gifs: Array<any> = [];
    public selectedIdiom = "Tamil"; 
    getTrendingGIFs(){
    this._homeserv.getTrendingGifs(this.selectedIdiom,1)
    .subscribe( (result) => { this.trendingGIFs = result ; this.gifs = this.gifs.concat(this.trendingGIFs.contents);},
                (err) => {  console.log(err); },
                () => console.log('trendingGifs',this.trendingGIFs))
    }
}
