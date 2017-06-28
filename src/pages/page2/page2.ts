import { Component,OnInit } from '@angular/core';
import { NavController, NavParams ,Events } from 'ionic-angular';
import { HomeService } from '../../services/home.service';
import { SearchComponent } from '../search/search.component';
import { GifDetailComponent } from '../home/gifdetail/gifdetail.component';
import { IdiomComponent } from '../idiom/idiom.component';
import { LoginPage } from '../login/login.component';
import { UploadComponent } from '../upload/upload.component';

@Component({
  selector: 'page-page2',
  templateUrl: 'page2.html'
})
export class Page2Page implements OnInit{
    // celebrity page
   public tabId;
  public selectedIdiom;
  newselectedIdiom;
   public tabIddata; 
     // public tabId = 1498144144545;
      
    constructor(public navCtrl: NavController, 
                public navparams: NavParams,
                public _homeserv : HomeService,
                public events : Events) {
              // this.events.subscribe('idiom:selected',(idiom) => {
               //  this.selectedIdiom = idiom;
                 
                 this.newselectedIdiom = this.navparams.data;
                 
                 console.log('newselectedIdiom',this.newselectedIdiom)
                this.selectedIdiom = this.newselectedIdiom.idiom;
                 console.log('selectedIdiom',this.selectedIdiom);
            //  });
             this.getTrendingGIFs();
                 
                        this.events.subscribe('tab:selected',(id) => {
                             this.tabId = id;
                             this.gettabdata(this.selectedIdiom,id);
                        });
                //   this.getTabdata(this.selectedIdiom,this.tabId);
               
                 
                }
                

     public trendingGIFs: any;
  public gifs: Array<any> = []; 
  getTrendingGIFs(){
 //this.cs.showLoader();
 //this.selectedIdiom = idiom;
 console.log('currentpage',1);
    this._homeserv.getTrendingGifs(this.selectedIdiom,1)
    .subscribe( (result) => { this.trendingGIFs = result ; this.gifs = this.gifs.concat(this.trendingGIFs.contents); },
                
    )}

gettabdata(idiom,tabid){
     this._homeserv.getTabDataviaTabId(idiom,tabid)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata},
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
    
                   console.log('tabId',this.tabId);
}

    
     navGifDetail(url){
    this.navCtrl.push(GifDetailComponent,{
      'url' : url,
      'idiom' : this.selectedIdiom
    });
  }

     ionViewDidEnter(){
      

     }

    ngOnInit(){
      // this.getTrendingGIFs();
   //   this.gettabdata();
    }
}
