import {Component,OnInit,Input,ViewChild} from '@angular/core';
import {NavParams,NavController} from 'ionic-angular';
import {SearchService} from '../../../services/search.service';
import {CustomService} from '../../../services/custom.service';
import {GifDetailComponent} from '../../home/gifdetail/gifdetail.component';

@Component({
    selector : 'page-search-result',
    templateUrl: 'search-result.html',
   // styleUrls : ['./search-result.scss']
})

export class SearchResultComponent implements OnInit {


    public searchItem;
    public searchedGifs = [];
    selectedIdiom;
    public currentPage = 1;
    constructor(private navparams : NavParams,
                private _searchService : SearchService,
                private cs : CustomService ,
                private navCtrl : NavController){ 
                this.selectedIdiom = this.navparams.get('idiom');
                
    } 

    getSearchGifs(item,selectedIdiom){
        this.cs.showLoader();
        console.log('selectedIdiom',this.selectedIdiom);
        this._searchService.GetGifsSearch(selectedIdiom,item)
        .subscribe( (res) => { this.searchedGifs = res; this.cs.hideLoader();  },
                    (err) => { console.log(err); this.cs.hideLoader();},
                    () => console.log('search gifs',this.searchedGifs))
    }

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


//     public doInfinite(infiniteScroll) {
//     this.currentPage += 1;
//     setTimeout(() => {
//       this.loadMoreData(infiniteScroll);
//     }, 500);
//   }

//   public loadMoreData(infiniteScroll) {
//     this._searchService.GetGifsSearch(this.selectedIdiom,1).subscribe((response) => {
//       if (response.status === 204) {
//         this.currentPage -= 1;
//         infiniteScroll.complete();
//         return;
//       }
//       infiniteScroll.complete();
//       this.allData = this.allData.concat(response);
//     }, (err) => {
//       infiniteScroll.complete();
//       this.currentPage -= 1;
//     //  this.onError(err);
//     });
//   }

} 