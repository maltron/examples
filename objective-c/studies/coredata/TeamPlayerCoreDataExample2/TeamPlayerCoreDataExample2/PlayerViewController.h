//
//  PlayerViewController.h
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "ListPersistViewController.h"
#import "EntityPersistent.h"
#import "Team.h"

@interface PlayerViewController : ListPersistViewController<EntityPersistent, UITableViewDelegate>
@property (nonatomic, strong) Team *teamSelected;

@end
