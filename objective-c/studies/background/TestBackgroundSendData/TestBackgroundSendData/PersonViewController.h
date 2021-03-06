//
//  MyViewController.h
//  HelloWorld
//
//  Created by Mauricio Leal on 1/11/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ListPersistViewController.h"
#import "EntityPersistent.h"

@interface PersonViewController : ListPersistViewController<EntityPersistent, UITableViewDelegate, NSFetchedResultsControllerDelegate>

@end
