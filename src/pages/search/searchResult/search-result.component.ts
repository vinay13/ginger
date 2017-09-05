import {Component,OnInit,Input,ViewChild,Renderer} from '@angular/core';
import {Platform,IonicPage,NavParams,NavController,Events,Nav} from 'ionic-angular';
import {SearchService} from '../../../services/search.service';
import {HomeService} from '../../../services/home.service';
import {CustomService} from '../../../services/custom.service';
import {GifDetailComponent} from '../../home/gifdetail/gifdetail.component';
import {AboutPage} from '../../about/about.ts';

// @IonicPage({
//     name: 'something-else',
//     segment: 'some-other-path'
// })

@Component({
    selector : 'page-search-result',
    templateUrl: 'search-result.html',
   // styleUrls : ['./search-result.scss']
})

export class SearchResultComponent implements OnInit {

    public searchItem;
    public searchedGifs = [];
    selectedIdiom;
    totalCount;
    lessdata;
    LessData;
    baseUrl = "https://gola-gif-dev-store-cf.xpresso.me/R2luZ2Vy/";
    constructor(private navparams : NavParams,
                private _searchService : SearchService,
                private _homeserv : HomeService,
                private cs : CustomService,
                private navCtrl : NavController,
                public events : Events,
                public renderer : Renderer,
                public nav : Nav,
                private platform: Platform){ 
                this.selectedIdiom = this.navparams.get('idiom');        
                // this.navCtrl.remove(1,1);
                events.subscribe('lessdata:created', (user) => {
                    console.log('Welcome', user);
                    this.lessdata = user;
                    this.LessData = this.lessdata;
                });

                this.lessdata = localStorage.getItem('lessdata');
                if(this.lessdata === "false"){
                    this.LessData = false
                } 
                else{
                    this.LessData = true;
                }  

                platform.ready().then(() => {
                    platform.registerBackButtonAction(() => {
                            if(this.nav.canGoBack()){
                             this.events.publish('reloadLayout');
                              this.navCtrl.pop(); 
                             
                        }
                    })
                })
    } 

    CustomNavRoot(){
        // this.navCtrl.setRoot(AboutPage,{
        // 'idiom': this.selectedIdiom
        // });
        //this.nav.popToRoot();
       
       // This will remove the 'ResultPage' from stack.
       this.navCtrl.pop(); 
       this.events.publish('reloadLayout');
    }
     
    getSearchGifs(item,selectedIdiom){
        this.cs.showLoader();
        console.log('selectedIdiom',this.selectedIdiom);
        this._searchService.GetGifsSearch(selectedIdiom,item,this.currentPage)
        .subscribe( (res) => { this.searchedGifs = res.contents; this.totalCount = res.totalCount ; this.cs.hideLoader();  },
                    (err) => { console.log(err); this.cs.hideLoader();},
                    () => console.log('search gifs',this.searchedGifs))
    }

    public gifData;
    GifsViewviaId(tabid){
      this.cs.showLoader();
      this._homeserv.getGifviaID(tabid)
          .subscribe((res) => { this.gifData = res; this.cs.hideLoader(); this.viewGif(res);},
                        (err) => { this.cs.hideLoader();} )
 
   }

    // public searchItem;
    ngOnInit(){
         this.searchItem = this.navparams.get('sitem') || this.navparams.get('tag');
         this.getSearchGifs(this.searchItem,this.selectedIdiom);
         //  this.searchedGifs = this.navparams.get('relatedgifs');
         //  console.log('seeergifs',this.searchedGifs);
    }

    someFunction(event: KeyboardEvent) { 
        let val = (<HTMLInputElement>event.target).value;
        this.getSearchGifs(val,this.selectedIdiom);

    //     this.navCtrl.push(SearchResultComponent,{
    //         'sitem' : val ,
    //         'relatedgifs' :  this.searchedGifs
    //   });
    }

    getItems(){
        return this.searchItem;
    }

    viewGif(url){
        this.navCtrl.push(GifDetailComponent,{
            'url': url,
            'idiom' : this.selectedIdiom
        });
    } 


currentPage = 0;    
 doInfinite(infiniteScroll) {

   this.currentPage = this.currentPage + 1;
    console.log('currentpage', this.currentPage);
       this._searchService.GetGifsSearch(this.selectedIdiom,this.searchItem,this.currentPage).subscribe(data =>
        {
          infiniteScroll.complete();
        //   this.hasMoreData = true;
        //   this.trendingGIFs = data;
          this.searchedGifs =  this.searchedGifs.concat(data.contents); 
      }, 
    err => {
      infiniteScroll.complete();
      this.currentPage -= 1;
   //   this.onError(err);
    },
     () => console.log('Next Page Loading completed')
     );
  }

} 