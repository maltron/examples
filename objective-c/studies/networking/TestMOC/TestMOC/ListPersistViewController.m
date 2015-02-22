//
//  PersistentViewController.m
//  TeamPlayerCoreDataExample
//
//  Created by Mauricio Leal on 1/1/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import "ListPersistViewController.h"
#import "AutoLayout.h"

@interface ListPersistViewController()

@end

@implementation ListPersistViewController
@synthesize tableView = _tableView;

-(void)loadView {
    [super loadView];
    
    _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
    [self.tableView setTranslatesAutoresizingMaskIntoConstraints:NO];
    [self.tableView setDelegate:self];
    [self.tableView setDataSource:self];
    [self.view addSubview:self.tableView];
    [self.view addConstraints:[AutoLayout fill:self.tableView inside:self.view]];
}

// TABLEVIEW DATASOURCE TABLEVIEW DATASOURCE TABLEVIEW DATASOURCE TABLEVIEW DATASOURCE
//  TABLEVIEW DATASOURCE TABLEVIEW DATASOURCE TABLEVIEW DATASOURCE TABLEVIEW DATASOURCE

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    NSLog(@">>> [ListPersistVC numberOfSectionsInTableView]");
    return [[self.fetchedResultsController sections] count];
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    NSLog(@">>> [ListPersistVC numberOfRowsInSection]");
    id <NSFetchedResultsSectionInfo> sectionInfo = [self.fetchedResultsController sections][section];
    return [sectionInfo numberOfObjects];
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@">>> [ListPersistVC cellForRowAtIndexPath]");
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell"];
    if(!cell) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
    }
    
    [self configureCell:cell atIndexPath:indexPath];
    
    return cell;
}

// TABLEVIEW DELEGATE TABLEVIEW DELEGATE TABLEVIEW DELEGATE TABLEVIEW DELEGATE TABLEVIEW
//   TABLEVIEW DELEGATE TABLEVIEW DELEGATE TABLEVIEW DELEGATE TABLEVIEW DELEGATE TABLEVIEW

-(BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    return YES;
}

-(void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    NSLog(@">>> [ListPersistVC commitEditingStyle]");
    
    if(editingStyle == UITableViewCellEditingStyleDelete) {
        NSManagedObjectContext *context = [self.fetchedResultsController managedObjectContext];
        [context deleteObject:[self.fetchedResultsController objectAtIndexPath:indexPath]];
        
        NSError *error = nil;
        if(![context save:&error]) {
            // Replace this implementation with code to handle the error
            // apropriately. abort() causes the application to generate
            // a crash log and terminate. You should not use this function
            // in a shipping application, altough it may be useful
            // during development.
            NSLog(@"Unresolved error %@, %@", error, [error userInfo]);
            abort();
        }
    }
}

-(BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // The table view should not be re-orderable.
    return NO;
}

// FETCHED RESULTS CONTROLLER DELEGATE FETCHED RESULTS CONTROLLER DELEGATE FETCHED RESULTS
//    FETCHED RESULTS CONTROLLER DELEGATE FETCHED RESULTS CONTROLLER DELEGATE FETCHED RESULTS

-(void)controllerWillChangeContent:(NSFetchedResultsController *)controller {
    NSLog(@">>> [ListPersistVC controllerWillChangeContent] tableView beginUpdates");
    [self.tableView beginUpdates];
}

-(void)controller:(NSFetchedResultsController *)controller didChangeSection:(id<NSFetchedResultsSectionInfo>)sectionInfo atIndex:(NSUInteger)sectionIndex forChangeType:(NSFetchedResultsChangeType)type {
    NSLog(@">>> [ListPersistVC didChangeSection:atIndex:forChangeType]");
    
    switch(type) {
        case NSFetchedResultsChangeInsert: // New Inserts ?
            [self.tableView insertSections:[NSIndexSet indexSetWithIndex:sectionIndex] withRowAnimation:UITableViewRowAnimationFade];
            break;
        case NSFetchedResultsChangeDelete: // New Deletes ?
            [self.tableView deleteSections:[NSIndexSet indexSetWithIndex:sectionIndex] withRowAnimation:UITableViewRowAnimationFade];
            break;
        case NSFetchedResultsChangeMove: // NOTHING TO DO
            break;
        case NSFetchedResultsChangeUpdate: // NOTHING TO DO
            break;
    }
}

-(void)controller:(NSFetchedResultsController *)controller didChangeObject:(id)anObject atIndexPath:(NSIndexPath *)indexPath forChangeType:(NSFetchedResultsChangeType)type newIndexPath:(NSIndexPath *)newIndexPath {
    NSLog(@">>> [ListPersistVC didChangeObject:atIndexPath:forChangeType:newIndexPath]");
    
    UITableView *tableView = self.tableView;
    switch(type) {
        case NSFetchedResultsChangeInsert:
            [tableView insertRowsAtIndexPaths:@[newIndexPath] withRowAnimation:UITableViewRowAnimationFade];
            break;
        case NSFetchedResultsChangeDelete:
            [tableView deleteRowsAtIndexPaths:@[indexPath]
                             withRowAnimation:UITableViewRowAnimationFade];
            break;
        case NSFetchedResultsChangeUpdate:
            [self configureCell:[tableView cellForRowAtIndexPath:indexPath] atIndexPath:indexPath];
            break;
        case NSFetchedResultsChangeMove:
            [tableView deleteRowsAtIndexPaths:@[indexPath]
                             withRowAnimation:UITableViewRowAnimationFade];
            [tableView insertRowsAtIndexPaths:@[newIndexPath] withRowAnimation:UITableViewRowAnimationFade];
            break;
    }
}

-(void)controllerDidChangeContent:(NSFetchedResultsController *)controller {
    NSLog(@">>> [ListPersistVC controllerDidChangeContent] tableView endUpdates");
    [self.tableView endUpdates];
}

// ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST
//  ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST ENTITY PERSIST

-(NSFetchedResultsController *)fetchedResultsController {
    // TO BE IMPLEMENTED
    return nil;
}

-(void)configureCell:(UITableViewCell *)celll atIndexPath:(NSIndexPath *)indexPath {
    // TO BE IMPLEMENTED
}


@end
