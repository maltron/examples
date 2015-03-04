//
//  PersonViewController.h
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "ListPersistViewController.h"
#import "EntityPersistent.h"

@interface PersonViewController : ListPersistViewController<EntityPersistent, UITableViewDelegate>

@end
