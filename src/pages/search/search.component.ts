import { Component } from '@angular/core';
import { NavController,NavParams} from 'ionic-angular';
import { SearchResultComponent } from './searchResult/search-result.component';
import { SearchService } from '../../services/search.service'; 


@Component({
    selector : 'page-search',
    templateUrl : 'search.html'
})

export class SearchComponent{

    searchQuery : string = '';
    items : string[];

    constructor(private navCtrl : NavController,
                private navParmas : NavParams,
                private _searchservice : SearchService){
                // this.initializeitems();
               //  this.getSuggestedItems(text);
              
    }

    // initializeitems(){
    //     this.items = [
    //         'Happy',
    //         'Happy Songs',
    //         'Random gifs',
    //         'Holla',
    //         'Hell'
    //     ];
    // }


     getItems(ev : any){
        
        let val = ev.target.value;
        this.getSuggestedItems(val);
         if (val && val.trim() != '') {
        this.suggestedText = this.suggestedText.filter((item) => {
        return (item.toLowerCase().indexOf(val.toLowerCase()) > -1);
          })
        }
     }

    public suggestedText = [];
    getSuggestedItems(val){
        this._searchservice.TextSuggestions(val)
        .subscribe( (data) => { this.suggestedText = data },
                    () =>  console.log('suggested text', this.suggestedText))
    }


    public searchedGifs = [];
    getSearchGifs(item){
        this._searchservice.GetGifsSearch(item)
        .subscribe( (res) => { this.searchedGifs = res },
                    () => console.log('related gifs',this.searchedGifs))
    }

    TagsClick(item){
         console.log('search gif with tag name',item);
         this.getSearchGifs(item);
         
         this.navCtrl.push(SearchResultComponent,{
            'sitem' : item ,
            'relatedgifs' :  this.searchedGifs
      });
    }
}