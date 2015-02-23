//
//  PersonService.h
//  TestMOC
//
//  Created by Mauricio Leal on 2/21/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NetworkService.h"
#import "Person.h"

@interface PersonService : NetworkService
@property (readonly, nonatomic, strong) NSManagedObjectContext *personContext;

-(void)fetchAll:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler ;
-(void)findFirstName:(NSString *)firstName andLastName:(NSString *)lastName completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *response, NSError *error))completionHandler;
-(void)save:(Person *)person isInsert:(BOOL)isInsert completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *respose, NSError *error))completionHandler;
-(void)delete:(NSManagedObjectID *)ID completionHandler:(void(^)(NSData *data, NSHTTPURLResponse *respose, NSError *error))completionHandler;

@end
