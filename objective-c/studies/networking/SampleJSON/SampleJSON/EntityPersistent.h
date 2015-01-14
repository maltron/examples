//
//  PersistentEntity.h
//  TeamPlayerCoreDataExample
//
//  Created by Mauricio Leal on 1/1/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

@protocol EntityPersistent <NSObject>

-(NSFetchedResultsController *)fetchedResultsController;
-(void)configureCell:(UITableViewCell *)cell atIndexPath:(NSIndexPath *)indexPath;

@end