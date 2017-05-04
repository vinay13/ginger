import { Component } from '@angular/core';

@Component({
    selector : 'page-search',
    templateUrl : 'search.html'
})


export class SearchComponent{

    searchQuery : string = '';
    items : string[];

    constructor(){
        this.initializeitems();
    }


    initializeitems(){
        this.items = [
            'Happy',
            'Happy Songs',
            'Random gifs',
            'Holla',
            'Hell'
        ];
    }

    getItems(ev : any){
        this.initializeitems();

        let val = ev.target.value;
         if (val && val.trim() != '') {
      this.items = this.items.filter((item) => {
        return (item.toLowerCase().indexOf(val.toLowerCase()) > -1);
      })
    }
}

TagsClick(){
    console.log('tags clicked');
}
}