import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { HomeService } from '../../services/home.service';

@Component({
  selector: 'page-page1',
  templateUrl: 'page1.html'
})
export class Page1Page {

 // rootNavCtrl: NavController;

  constructor(public navCtrl: NavController,
              public navParams: NavParams,
              public _homeserv : HomeService){
                this.getTrendingGIFs();
            //  this.rootNavCtrl = navParams.get('rootNavCtrl');
             
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
