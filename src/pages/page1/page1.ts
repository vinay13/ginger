import { Component } from '@angular/core';
import { NavController, NavParams } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';

@Component({
  selector: 'page-page1',
  templateUrl: 'page1.html'
})

export class Page1Page {

    rootNavCtrl: NavController;
    public selectedIdiom;
    public newselectedIdiom;
    constructor(public navCtrl: NavController,
                public navparams: NavParams,
                public _homeserv : HomeService){
                             
                this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                this.newselectedIdiom = this.navparams.data;
                this.selectedIdiom = this.newselectedIdiom.idiom;
             
                this.tabcat();
    }
            

    public trendingGIFs: any;
    public gifs: Array<any> = [];
  
    public tabdata;
    public tabcat(){
         this._homeserv.getTabCategories(this.selectedIdiom)
                    .subscribe( (res) => { this.tabdata = res.tabs; this.gettabdata(this.selectedIdiom,this.tabdata[0].id);  },
                                (err) => { console.log(err)},
                                () => { console.log('tabdata',this.tabdata[0].id)})
    }

    public tabIddata;
    gettabdata(idiom,tabid){
       this.tabIddata = [];
       this._homeserv.getTabDataviaTabId(idiom,tabid)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata; },
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
    }

    navGifDetail(url){
      this.rootNavCtrl.push(GifDetailComponent,{
        'url' : url,
        'idiom' : this.selectedIdiom
      });
    }
     

}
