import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { HomeService } from '../../services/home.service';

@Component({
  selector: 'page-page3',
  templateUrl: 'page3.html'
})

export class Page3Page {

  selectedIdiom:any;
  constructor(public navCtrl: NavController,
               public navParams: NavParams,
               public _homeserv : HomeService) { 
                 this.tabcat();
               }

    public tabdata;
    public tabcat(){
         this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe( (res) => { this.tabdata = res.tabs; this.gettabdata(this.selectedIdiom,this.tabdata[2].id);  },
                                (err) => { console.log(err)},
                                () => { console.log('tabdata',this.tabdata[0].id)})
    }

    public tabIddata;
    public gifs;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
       this._homeserv.getTabDataviaTabId(idiom,tabid)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
    }
}
