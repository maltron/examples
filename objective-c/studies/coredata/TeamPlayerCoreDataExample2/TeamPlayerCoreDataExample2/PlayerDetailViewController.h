//
//  PlayerDetailViewController.h
//  TeamPlayerCoreDataExample2
//
//  Created by Mauricio Leal on 1/8/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "EntityDetailViewController.h"
#import "Team.h"
#import "Player.h"

@interface PlayerDetailViewController : EntityDetailViewController<UITableViewDataSource>
@property (nonatomic, strong) Team *teamSelected;
@property (nonatomic, strong) Player *playerSelected;

-(void)newPlayer;

@end
