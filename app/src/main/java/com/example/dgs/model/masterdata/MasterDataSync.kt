package com.example.dgs.model.masterdata

data class MasterDataSync (var board_hash:String,
                           var medium_hash:String,
                           var standard_hash:String,
                           var subject_hash:String,
                           var publisher_hash:String,
                           var author_hash:String,
                           var board_list:ArrayList<BoardList>,
                           var medium_list:ArrayList<MediumList>,
                           var standard_list:ArrayList<StandardList>,
                           var subject_list:ArrayList<SubjectList>,
                           var publisher_list:ArrayList<PublisherList>,
                           var author_list:ArrayList<AuthorList>,
                           var by_class:ArrayList<ByClass>,
                           var by_board:ArrayList<ByBoard>)
{
}