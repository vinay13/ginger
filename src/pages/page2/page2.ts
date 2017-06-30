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
   public tabIDD;
  public selectedIdiom;
  newselectedIdiom;
   public tabIddata; 
     // public tabId = 1498144144545;
      rootNavCtrl: NavController;
    constructor(public navCtrl: NavController, 
                public navparams: NavParams,
                public _homeserv : HomeService,
                public events : Events) {
              // this.events.subscribe('idiom:selected',(idiom) => {
               //  this.selectedIdiom = idiom;
                   this.rootNavCtrl = this.navparams.get('rootNavCtrl');
                 this.newselectedIdiom = this.navparams.data;
                this.selectedIdiom = this.newselectedIdiom.idiom;
                this.tabIDD = this.newselectedIdiom.tabID;
                 console.log('selectedIdiom',this.selectedIdiom);
                  console.log('tabIDD',this.tabIDD);
            //  });
              //  this.getTrendingGIFs();
                this.gettabdata(this.selectedIdiom,this.tabIDD);   
                this.events.subscribe('tab:selected',(id) => {
                             this.tabId = id;
                             this.gettabdata(this.selectedIdiom,id);
                        });
                //   this.getTabdata(this.selectedIdiom,this.tabId);
               
                 
                }
                

  // public trendingGIFs: any;
   public gifs: Array<any> = []; 
  // getTrendingGIFs(){
  // console.log('currentpage',1);
  //   this._homeserv.getTrendingGifs(this.selectedIdiom,1)
  //   .subscribe( (result) => { this.trendingGIFs = result ; this.gifs = this.gifs.concat(this.trendingGIFs.contents); },           
  //   )}

gettabdata(idiom,tabid){
     this._homeserv.getTabDataviaTabId(idiom,tabid)
                  .subscribe((res) => {this.tabIddata = res ; this.gifs = this.tabIddata},
                  (err) => {console.log(err)},
                  () => console.log('data',this.tabIddata ))
}

    
     navGifDetail(url){
       console.log('navgifdetail',this.selectedIdiom);
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
    console.log("next page:"+nextpage)
    this._homeserv.getTrendingGifs(this.selectedIdiom,nextpage).subscribe(
            data => {
              infiniteScroll.complete();
                this.trendingGIFs = data;
                console.log('scroll',this.trendingGIFs );
                // for(let post of posts){
                   this.gifs =  this.gifs.concat(this.trendingGIFs.contents); 
                // }
            },
            err => {
                console.log(err);
            },
            () => console.log('Next Page Loading completed')
        );
  infiniteScroll.complete();
} 


    ngOnInit(){
     
      // this.getTrendingGIFs();
   //   this.gettabdata();
    }
}
