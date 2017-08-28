import {Component,OnInit,Input,ViewChild,Renderer} from '@angular/core';
import {IonicPage,NavParams,NavController,Events} from 'ionic-angular';
import {SearchService} from '../../../services/search.service';
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

    constructor(private navparams : NavParams,
                private _searchService : SearchService,
                private cs : CustomService,
                private navCtrl : NavController,
                public events : Events,
                public renderer : Renderer){ 
                this.selectedIdiom = this.navparams.get('idiom');        

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
    } 

    CustomNavRoot(){
        this.navCtrl.setRoot(AboutPage,{
                'idiom': this.selectedIdiom
        });

       // this.navCtrl.push('something-else');
    }
     

    getSearchGifs(item,selectedIdiom){
        this.cs.showLoader();
        console.log('selectedIdiom',this.selectedIdiom);
        this._searchService.GetGifsSearch(selectedIdiom,item,this.currentPage)
        .subscribe( (res) => { this.searchedGifs = res.contents; this.totalCount = res.totalCount ; this.cs.hideLoader();  },
                    (err) => { console.log(err); this.cs.hideLoader();},
                    () => console.log('search gifs',this.searchedGifs))
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