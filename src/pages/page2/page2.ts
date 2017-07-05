import { Component,OnInit } from '@angular/core';
import { NavController, NavParams ,Events } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { SearchComponent } from '../search/search.component';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';
import { IdiomComponent } from '../idiom/idiom.component';
import { LoginPage } from '../login/login.component';
import { UploadComponent } from '../upload/upload.component';
import {CustomService} from '../../services/custom.service';

//declare var imagesLoaded : any;

@Component({
  selector: 'page-page2',
  templateUrl: 'page2.html'
})

export class Page2Page implements OnInit{
   public tabId;
   public tabIDD;
   public selectedIdiom;
   newselectedIdiom;
   public tabIddata; 
    
    rootNavCtrl: NavController;
    constructor(public navCtrl: NavController, 
                public navparams: NavParams,
                public _homeserv : HomeService,
                public events : Events,
                public cs : CustomService) {
            
                this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                this.newselectedIdiom = this.navparams.data;
                this.selectedIdiom = this.newselectedIdiom.idiom;
                this.tabIDD = this.newselectedIdiom.tabID;
      
                this.gettabdata(this.selectedIdiom,this.tabIDD);   
                this.events.subscribe('tab:selected',(id) => {
                             this.tabId = id;
                     //        this.gifs = tabdatas;
                             this.gettabdata(this.selectedIdiom,id);
                            this.events.unsubscribe('tab:selected');
                    });        
                }
                
   public gifs: Array<any> = []; 


  gettabdata(idiom,tabid){
     this._homeserv.getTabDataviaTabId(idiom,tabid)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata},
                  (err) => {console.log(err)})
  }

  navGifDetail(url){
    this.rootNavCtrl.push(GifDetailComponent,{
      'url' : url,
      'idiom' : this.selectedIdiom
    });
  }

  ionViewDidEnter(){
  }

  public pageno = 1;
  public trendingGIFs;
  doInfinite(infiniteScroll) {

    let nextpage=this.pageno++;
    this._homeserv.getTrendingGifs(this.selectedIdiom,nextpage).subscribe(
            data => {
              infiniteScroll.complete();
                this.trendingGIFs = data;
                console.log('scroll',this.trendingGIFs );
                   this.gifs =  this.gifs.concat(this.trendingGIFs.contents); 
            },
            err => {
                console.log(err);
            },
            () => console.log('Next Page Loading completed')
        );
  infiniteScroll.complete();
} 

  UploadviaWeb(){
    this.cs.showLoader();
    this.navCtrl.push(UploadComponent);
    this.cs.hideLoader();
  }

    ngOnInit(){
   
    }
}
